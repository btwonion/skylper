package dev.nyon.skylper.extensions.render

/*? if >1.20.5 {*/
import dev.nyon.skylper.extensions.event.RenderHudEvent
import dev.nyon.skylper.extensions.event.invokeEvent
import dev.nyon.skylper.minecraft
//? if >=1.21
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw

object CustomRenderLayer : LayeredDraw.Layer {
    override fun render(
        guiGraphics: GuiGraphics, /*? if >=1.21 {*/ deltaTracker: DeltaTracker /*?} else {*/ /*f: Float *//*?}*/
    ) {
        if (minecraft.gui.debugOverlay.showDebugScreen()) return
        invokeEvent(RenderHudEvent(guiGraphics))
    }
}/*?}*/