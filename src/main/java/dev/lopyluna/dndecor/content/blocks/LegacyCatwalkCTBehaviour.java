package dev.lopyluna.dndecor.content.blocks;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.decoration.encasing.CasingConnectivity;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Original top/bottom connected-texture rule used by the 0.4.0b catwalks. */
public class LegacyCatwalkCTBehaviour extends ConnectedTextureBehaviour.Base {
    private final CTSpriteShiftEntry shift;

    public LegacyCatwalkCTBehaviour(CTSpriteShiftEntry shift) {
        this.shift = shift;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader,
                              BlockPos pos, BlockPos otherPos, Direction face) {
        if (isBeingBlocked(state, reader, pos, otherPos, face) || pos.getY() != otherPos.getY()) return false;
        if (face != Direction.UP && face != Direction.DOWN) return false;
        CasingConnectivity.Entry entry = CreateClient.CASING_CONNECTIVITY.get(state);
        CasingConnectivity.Entry otherEntry = CreateClient.CASING_CONNECTIVITY.get(other);
        return entry != null && otherEntry != null
                && entry.isSideValid(state, face) && otherEntry.isSideValid(other, face)
                && entry.getCasing() == otherEntry.getCasing();
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction,
                                       @Nullable TextureAtlasSprite sprite) {
        return shift;
    }
}
