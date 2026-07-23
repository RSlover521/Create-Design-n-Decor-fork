package dev.lopyluna.dndecor.content.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/** Restored implementation of the decorative plates called signs in DnDecor 0.4.0b. */
public class LegacySignBlock extends DirectionalBlock implements IWrenchable {
    /** In-plane orientation used when the sign is attached to a floor or ceiling. */
    public static final DirectionProperty ROTATION = DirectionProperty.create("rotation", Direction.Plane.HORIZONTAL);
    private static final VoxelShape UP = Block.box(0, 15, 0, 16, 16, 16);
    private static final VoxelShape DOWN = Block.box(0, 0, 0, 16, 1, 16);
    private static final VoxelShape NORTH = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape SOUTH = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape WEST = Block.box(0, 0, 0, 1, 16, 16);
    private static final VoxelShape EAST = Block.box(15, 0, 0, 16, 16, 16);

    public LegacySignBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(ROTATION, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROTATION);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeFor(state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeFor(state.getValue(FACING));
    }

    private static VoxelShape shapeFor(Direction direction) {
        return switch (direction) {
            case DOWN -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> UP;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // FACING points toward the supporting block. Using the clicked face first is
        // important for vertical placement: clicking a block's top attaches the sign
        // to the floor (DOWN), while clicking its underside attaches it to the ceiling
        // (UP), independent of the player's pitch or nearest-looking direction order.
        Direction attachment = context.getClickedFace().getOpposite();
        Direction rotation = context.getHorizontalDirection().getOpposite();
        BlockState clickedFaceState = defaultBlockState()
                .setValue(FACING, attachment)
                .setValue(ROTATION, rotation);
        if (clickedFaceState.canSurvive(context.getLevel(), context.getClickedPos()))
            return clickedFaceState;

        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState state = defaultBlockState()
                    .setValue(FACING, direction)
                    .setValue(ROTATION, rotation);
            if (state.canSurvive(context.getLevel(), context.getClickedPos()))
                return state;
        }
        return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !level.getBlockState(pos.relative(state.getValue(FACING))).isAir();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return canSurvive(state, level, pos)
                ? super.updateShape(state, direction, neighborState, level, pos, neighborPos)
                : Blocks.AIR.defaultBlockState();
    }
}
