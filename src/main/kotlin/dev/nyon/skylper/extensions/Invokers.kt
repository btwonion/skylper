package dev.nyon.skylper.extensions

import com.mojang.blaze3d.vertex.PoseStack
import dev.nyon.skylper.asm.invokers.BeaconRendererInvoker
import net.minecraft.client.renderer.MultiBufferSource

fun internalRenderBeaconBeam(
    poseStack: PoseStack,
    bufferSource: MultiBufferSource,
    partialTick: Float,
    gameTime: Long,
    color: Int,
    height: Int,
    yOffset: Int = 0
) {
    BeaconRendererInvoker.renderBeaconBeam(
        poseStack,
        bufferSource,
        partialTick,
        gameTime,
        yOffset,
        height,
        color.color.getRGBComponents(null)
    )
}
