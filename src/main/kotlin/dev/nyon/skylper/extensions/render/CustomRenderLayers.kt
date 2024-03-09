package dev.nyon.skylper.extensions.render

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType

object CustomRenderLayers {
    private val transparency = TransparencyStateShard("skylper_transparency", {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
    }, RenderSystem::disableBlend)

    val filled: RenderType.CompositeRenderType = RenderType.create(
        "skylper_filled",
        DefaultVertexFormat.POSITION_COLOR,
        VertexFormat.Mode.TRIANGLE_STRIP,
        RenderType.SMALL_BUFFER_SIZE,
        false,
        true,
        RenderType.CompositeState.builder().setShaderState(POSITION_COLOR_SHADER).setCullState(NO_CULL)
            .setLayeringState(POLYGON_OFFSET_LAYERING).setTransparencyState(transparency)
            .setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(false)
    )

    val filledThroughWall: RenderType.CompositeRenderType = RenderType.create(
        "skylper_filled_through_wall",
        DefaultVertexFormat.POSITION_COLOR,
        VertexFormat.Mode.TRIANGLE_STRIP,
        RenderType.SMALL_BUFFER_SIZE,
        false,
        true,
        RenderType.CompositeState.builder().setShaderState(POSITION_COLOR_SHADER).setCullState(NO_CULL)
            .setLayeringState(POLYGON_OFFSET_LAYERING).setTransparencyState(transparency)
            .setDepthTestState(NO_DEPTH_TEST).createCompositeState(false)
    )
}