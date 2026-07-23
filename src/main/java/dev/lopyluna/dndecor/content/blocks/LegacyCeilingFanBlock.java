package dev.lopyluna.dndecor.content.blocks;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import dev.lopyluna.dndecor.register.DnDecorBETypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Restored ceiling fan kinetic connection from Design n' Decor 0.4.0b. */
public class LegacyCeilingFanBlock extends KineticBlock implements IBE<BracketedKineticBlockEntity> {
    public LegacyCeilingFanBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public Class<BracketedKineticBlockEntity> getBlockEntityClass() {
        return BracketedKineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BracketedKineticBlockEntity> getBlockEntityType() {
        return DnDecorBETypes.CEILING_FAN.get();
    }
}
