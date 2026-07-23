package dev.lopyluna.dndecor.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Exact axis-dependent outline from Design n' Decor 0.4.0b's industrial gears. */
public class LegacyIndustrialGearBlock extends RotatedPillarBlock {
    private static final VoxelShape LARGE_X = Shapes.or(Block.box(2, -2, -2, 14, 18, 18), Block.box(0, 5, 5, 16, 11, 11));
    private static final VoxelShape LARGE_Y = Shapes.or(Block.box(-2, 2, -2, 18, 14, 18), Block.box(5, 0, 5, 11, 16, 11));
    private static final VoxelShape LARGE_Z = Shapes.or(Block.box(-2, -2, 2, 18, 18, 14), Block.box(5, 5, 0, 11, 11, 16));
    private static final VoxelShape SMALL_X = Shapes.or(Block.box(2, 1, 1, 14, 15, 15), Block.box(0, 5, 5, 16, 11, 11));
    private static final VoxelShape SMALL_Y = Shapes.or(Block.box(1, 2, 1, 15, 14, 15), Block.box(5, 0, 5, 11, 16, 11));
    private static final VoxelShape SMALL_Z = Shapes.or(Block.box(1, 1, 2, 15, 15, 14), Block.box(5, 5, 0, 11, 11, 16));

    private final boolean large;

    public LegacyIndustrialGearBlock(BlockBehaviour.Properties properties, boolean large) {
        super(properties);
        this.large = large;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction.Axis axis = state.getValue(AXIS);
        if (large)
            return axis == Direction.Axis.X ? LARGE_X : axis == Direction.Axis.Y ? LARGE_Y : LARGE_Z;
        return axis == Direction.Axis.X ? SMALL_X : axis == Direction.Axis.Y ? SMALL_Y : SMALL_Z;
    }
}
