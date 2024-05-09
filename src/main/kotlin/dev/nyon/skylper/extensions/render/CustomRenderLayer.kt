package dev.nyon.skylper.extensions.render

import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.RenderHudEvent
import dev.nyon.skylper.minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw

object CustomRenderLayer : LayeredDraw.Layer {
    override fun render(
        guiGraphics: GuiGraphics,
        f: Float
    ) {
        if (minecraft.gui.debugOverlay.showDebugScreen()) return
        EventHandler.invokeEvent(RenderHudEvent(guiGraphics))
    }
}
