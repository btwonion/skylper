package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.RenderHudEvent
import dev.nyon.skylper.extensions.render.hud.HudWidget
import dev.nyon.skylper.skyblock.mining.hollows.render.TotalPowderWidget
import dev.nyon.skylper.skyblock.mining.hollows.tracker.nucleus.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.mining.hollows.tracker.powder.PowderGrindingTracker
import net.minecraft.client.gui.GuiGraphics

object SkylperHud {
    fun init() {
        listenEvent<RenderHudEvent, Unit> {
            context.renderWidget(CrystalCompletionWidget)
            context.renderWidget(PowderGrindingTracker)
            context.renderWidget(TotalPowderWidget)
        }
    }

    private fun GuiGraphics.renderWidget(
        widget: HudWidget
    ) {
        if (!widget.shouldRender()) return
        widget.render(this, 0, 0)
    }
}
