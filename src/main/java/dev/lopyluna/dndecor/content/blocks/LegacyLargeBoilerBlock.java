package dev.lopyluna.dndecor.content.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/** Restored master block for the 0.4.0b three-block-wide boiler model. */
public class LegacyLargeBoilerBlock extends LegacyBoilerBlock {
    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");

    public LegacyLargeBoilerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(EXTENSION, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTENSION);
    }
}
