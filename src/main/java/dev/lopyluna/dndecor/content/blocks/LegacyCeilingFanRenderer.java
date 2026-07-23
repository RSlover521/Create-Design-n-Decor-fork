package dev.lopyluna.dndecor.content.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.lopyluna.dndecor.register.client.DnDecorPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

/** Renders the restored blade-and-shaft partial when Flywheel instancing is unavailable. */
public class LegacyCeilingFanRenderer extends KineticBlockEntityRenderer<BracketedKineticBlockEntity> {
    private static final PartialModel FAN = DnDecorPartialModels.CEILING_FAN;

    public LegacyCeilingFanRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(BracketedKineticBlockEntity be, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffer, int light, int overlay) {
        renderRotatingBuffer(be,
                CachedBuffers.partialFacingVertical(AllPartialModels.SHAFT_HALF, be.getBlockState(), Direction.UP),
                poseStack, buffer.getBuffer(RenderType.solid()), light);
        renderRotatingBuffer(be, CachedBuffers.partial(FAN, be.getBlockState()),
                poseStack, buffer.getBuffer(RenderType.cutout()), light);
    }
}
