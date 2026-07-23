package dev.lopyluna.dndecor.content.blocks;

import dev.lopyluna.dndecor.register.DnDecorShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/** State-compatible implementations for registry entries removed after Design n' Decor 0.4.0b. */
public final class LegacyMetalDecorationBlocks {
    private LegacyMetalDecorationBlocks() {}

    public static class Lamp extends LegacyDirectionalBlock {
        private static final VoxelShape DOWN = Block.box(4, 14, 4, 12, 16, 12);
        private static final VoxelShape UP = Block.box(4, 0, 4, 12, 2, 12);
        private static final VoxelShape EAST = Block.box(0, 4, 4, 2, 12, 12);
        private static final VoxelShape WEST = Block.box(14, 4, 4, 16, 12, 12);
        private static final VoxelShape NORTH = Block.box(4, 4, 14, 12, 12, 16);
        private static final VoxelShape SOUTH = Block.box(4, 4, 0, 12, 12, 2);
        public Lamp(BlockBehaviour.Properties properties) { super(properties); }
        @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return directionalShape(state.getValue(FACING), DOWN, UP, EAST, WEST, NORTH, SOUTH);
        }
        @Override public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
            return hasSupport(state, level, pos);
        }
        @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                                 LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
            return canSurvive(state, level, pos) ? super.updateShape(state, direction, neighbor, level, pos, neighborPos)
                    : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
            for (Direction clicked : context.getNearestLookingDirections()) {
                BlockState state = defaultBlockState().setValue(FACING, clicked.getOpposite());
                if (state.canSurvive(context.getLevel(), context.getClickedPos())) return state;
            }
            return null;
        }
    }

    public static class Screw extends LegacyDirectionalBlock {
        public static final BooleanProperty ROTATED = BooleanProperty.create("rotated");
        public Screw(BlockBehaviour.Properties properties) {
            super(properties);
            registerDefaultState(defaultBlockState().setValue(ROTATED, false));
        }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder); builder.add(ROTATED);
        }
    }

    public static class Floodlight extends DirectionalBlock implements SimpleWaterloggedBlock {
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
        public static final BooleanProperty TURNED_ON = BooleanProperty.create("turned_on");
        public static final BooleanProperty WRENCHED = BooleanProperty.create("wrenched");
        private static final VoxelShape DOWN = Block.box(3, 8, 3, 13, 16, 13);
        private static final VoxelShape UP = Block.box(3, 0, 3, 13, 8, 13);
        private static final VoxelShape EAST = Block.box(0, 3, 3, 8, 13, 13);
        private static final VoxelShape WEST = Block.box(8, 3, 3, 16, 13, 13);
        private static final VoxelShape NORTH = Block.box(3, 3, 8, 13, 13, 16);
        private static final VoxelShape SOUTH = Block.box(3, 3, 0, 13, 13, 8);
        public Floodlight(BlockBehaviour.Properties properties) {
            super(properties);
            registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                    .setValue(WATERLOGGED, false).setValue(TURNED_ON, false).setValue(WRENCHED, false));
        }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(FACING, WATERLOGGED, TURNED_ON, WRENCHED);
        }
        @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
            return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite())
                    .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        }
        @Override public FluidState getFluidState(BlockState state) {
            return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
        }
        @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return directionalShape(state.getValue(FACING), DOWN, UP, EAST, WEST, NORTH, SOUTH);
        }
        @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                                 LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
            if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            return canSurvive(state, level, pos) ? super.updateShape(state, direction, neighbor, level, pos, neighborPos)
                    : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        @Override public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
            return hasSupport(state, level, pos);
        }
    }

    public static class Catwalk extends Block implements SimpleWaterloggedBlock {
        // The old serializer called the lowered layer "turned_on".
        public static final BooleanProperty DOWN = BooleanProperty.create("turned_on");
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
        public Catwalk(BlockBehaviour.Properties properties) {
            super(properties); registerDefaultState(stateDefinition.any().setValue(DOWN, false).setValue(WATERLOGGED, false));
        }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(DOWN, WATERLOGGED); }
        @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
            return defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        }
        @Override public FluidState getFluidState(BlockState state) { return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state); }
        @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return state.getValue(DOWN) ? DnDecorShapes.CATWALK_DOWN : DnDecorShapes.CATWALK;
        }
    }

    public static class Railing extends Block implements SimpleWaterloggedBlock {
        public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
        public static final BooleanProperty EAST = BlockStateProperties.EAST;
        public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
        public static final BooleanProperty WEST = BlockStateProperties.WEST;
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
        private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 0, 16, 16, 2);
        private static final VoxelShape EAST_SHAPE = Block.box(14, 0, 0, 16, 16, 16);
        private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 14, 16, 16, 16);
        private static final VoxelShape WEST_SHAPE = Block.box(0, 0, 0, 2, 16, 16);
        public Railing(BlockBehaviour.Properties properties) {
            super(properties); registerDefaultState(stateDefinition.any().setValue(NORTH, false).setValue(EAST, false)
                    .setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
        }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED); }
        @Override public FluidState getFluidState(BlockState state) { return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state); }
        @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
            boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
            BlockState state = defaultBlockState().setValue(WATERLOGGED, waterlogged);
            return switch (context.getHorizontalDirection()) {
                case NORTH -> state.setValue(NORTH, true);
                case EAST -> state.setValue(EAST, true);
                case SOUTH -> state.setValue(SOUTH, true);
                case WEST -> state.setValue(WEST, true);
                default -> state;
            };
        }
        @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            VoxelShape shape = Shapes.empty();
            if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
            if (state.getValue(EAST)) shape = Shapes.or(shape, EAST_SHAPE);
            if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
            if (state.getValue(WEST)) shape = Shapes.or(shape, WEST_SHAPE);
            return shape;
        }
    }

    public static class Container extends Block {
        public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
        public static final BooleanProperty LARGE = BooleanProperty.create("large");
        public Container(BlockBehaviour.Properties properties) {
            super(properties); registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X).setValue(LARGE, false));
        }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(AXIS, LARGE); }
        @Override public BlockState getStateForPlacement(BlockPlaceContext context) { return defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getAxis()); }
    }

    private static VoxelShape directionalShape(Direction direction, VoxelShape down, VoxelShape up,
                                                VoxelShape east, VoxelShape west,
                                                VoxelShape north, VoxelShape south) {
        return switch (direction) {
            case DOWN -> down;
            case UP -> up;
            case EAST -> east;
            case WEST -> west;
            case NORTH -> north;
            case SOUTH -> south;
        };
    }

    private static boolean hasSupport(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(DirectionalBlock.FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }
}
