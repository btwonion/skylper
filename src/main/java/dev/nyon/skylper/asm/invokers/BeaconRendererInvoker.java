package dev.nyon.skylper.asm.invokers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeaconRenderer.class)
public interface BeaconRendererInvoker {

    @Invoker("renderBeaconBeam")
    static void renderBeaconBeam(
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        float partialTick,
        long gameTime,
        int yOffset,
        int height,
        float[] colors
    ) {
    }
}
