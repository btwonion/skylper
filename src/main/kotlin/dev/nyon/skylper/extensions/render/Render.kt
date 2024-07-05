package dev.nyon.skylper.extensions.render

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import dev.nyon.skylper.extensions.color
import dev.nyon.skylper.extensions.internalRenderBeaconBeam
import dev.nyon.skylper.extensions.isVisible
import dev.nyon.skylper.minecraft
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.lwjgl.opengl.GL11
import kotlin.math.pow

private const val MAX_BUILD_HEIGHT: Double = 319.0

fun WorldRenderContext.renderText(
    text: Component, pos: Vec3, scale: Float, throughWalls: Boolean = true
) {
    val matrices = matrixStack()!!
    val camera = camera()
    val correctedPos = pos.subtract(camera.position)
    val font = minecraft.font

    matrices.pushPose()
    matrices.translate(correctedPos.x, correctedPos.y, correctedPos.z)
    matrices.mulPose(camera.rotation())
    matrices.scale(-(scale * 0.025F), -(scale * 0.025F), -1F)

    val positionMatrix = matrices.last().pose()
    val xOffset = -font.width(text) / 2f
    val yOffset = -font.lineHeight / 2f

    val tes = RenderSystem.renderThreadTesselator()
    val builder = /*? if >=1.21 {*/ tes.buffer /*?} else {*/ /*tes.builder *//*?}*/
    val consumers = MultiBufferSource.immediate(builder)

    RenderSystem.depthFunc(if (throughWalls) GL11.GL_ALWAYS else GL11.GL_LEQUAL)

    font.drawInBatch(
        text,
        xOffset,
        yOffset,
        0xFFFFFF,
        false,
        positionMatrix,
        consumers,
        Font.DisplayMode.SEE_THROUGH,
        0xFFFFFFF,
        LightTexture.FULL_BRIGHT
    )
    consumers.endBatch()

    RenderSystem.depthFunc(GL11.GL_LEQUAL)
    matrices.popPose()
}

fun WorldRenderContext.renderBeaconBeam(
    pos: Vec3, color: Int
) {
    if (!minecraft.isVisible(AABB(pos.x, pos.y, pos.z, pos.x + 1, MAX_BUILD_HEIGHT, pos.z + 1))) return
    val matrices = matrixStack()!!
    val cameraPos = camera().position

    matrices.pushPose()
    matrices.translate(pos.x - cameraPos.x, pos.y - cameraPos.y, pos.z - cameraPos.z)

    internalRenderBeaconBeam(matrices, consumers()!!, /*? if >=1.21 {*/ tickCounter().getGameTimeDeltaPartialTick(true) /*?} else {*/ /*tickDelta() *//*?}*/ , world().gameTime, color, MAX_BUILD_HEIGHT.toInt())

    matrices.popPose()
}

fun WorldRenderContext.renderFilled(
    box: AABB, color: Int, throughWalls: Boolean = false
) = renderCustomWithBox(box, throughWalls) { matrices ->
    val tes = RenderSystem.renderThreadTesselator()
    //? if <1.21
    /*val builder = tes.builder*/
    val javaColor = color.color
    /*? if >=1.21 {*/ val builder = tes.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR)/*?} else {*//*builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR)*//*?}*/
    LevelRenderer.addChainedFilledBoxVertices(
        matrices,
        builder,
        box.minX,
        box.minY,
        box.minZ,
        box.maxX,
        box.maxY,
        box.maxZ,
        javaColor.red.toFloat() / 255,
        javaColor.green.toFloat() / 255,
        javaColor.blue.toFloat() / 255,
        0.2f
    )
    /*? if >=1.21 {*/ tes.clear() /*?} else {*/ /*tes.end() *//*?}*/
}

fun WorldRenderContext.renderOutline(
    box: AABB, color: Int, throughWalls: Boolean
) = renderCustomWithBox(box, throughWalls) { matrices ->
    RenderSystem.setShader(GameRenderer::getRendertypeLinesShader)
    RenderSystem.lineWidth(10f / camera().position.distanceToSqr(box.center).pow(0.25).toFloat())

    val tes = RenderSystem.renderThreadTesselator()
    //? if <1.21
    /*val builder = tes.builder*/
    val javaColor = color.color
    /*? if >=1.21 {*/ val builder = tes.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR)/*?} else {*//*builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR)*//*?}*/
    LevelRenderer.renderLineBox(
        matrices,
        builder,
        box.minX,
        box.minY,
        box.minZ,
        box.maxX,
        box.maxY,
        box.maxZ,
        javaColor.red.toFloat() / 255,
        javaColor.green.toFloat() / 255,
        javaColor.blue.toFloat() / 255,
        0.2f
    )
    /*? if >=1.21 {*/ tes.clear() /*?} else {*/ /*tes.end() *//*?}*/
}

fun WorldRenderContext.renderCustomWithBox(box: AABB, throughWalls: Boolean, block: (matrices: PoseStack) -> Unit) {
    if (!minecraft.isVisible(box)) return
    val matrices = matrixStack()!!
    val cameraPos = camera().position

    matrices.pushPose()
    matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)

    RenderSystem.setShader(GameRenderer::getPositionColorShader)
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.polygonOffset(-1f, -10f)
    RenderSystem.enablePolygonOffset()
    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
    RenderSystem.enableDepthTest()
    RenderSystem.depthFunc(if (throughWalls) GL11.GL_ALWAYS else GL11.GL_LEQUAL)
    RenderSystem.disableCull()

    block(matrices)

    matrices.popPose()
    RenderSystem.depthFunc(GL11.GL_LEQUAL)
    RenderSystem.polygonOffset(0f, 0f)
    RenderSystem.disablePolygonOffset()
    RenderSystem.disableBlend()
    RenderSystem.enableCull()
}
