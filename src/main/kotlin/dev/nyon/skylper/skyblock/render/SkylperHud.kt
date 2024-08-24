package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.extensions.event.RenderHudEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.render.hud.HudWidget
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.TotalPowderWidget
import dev.nyon.skylper.skyblock.tracker.mining.crystalHollows.powder.PowderGrindingTracker
import net.minecraft.client.gui.GuiGraphics

object SkylperHud {
    @SkylperEvent
    fun renderHudEvent(event: RenderHudEvent) {
        val context = event.context
        context.renderWidget(CrystalCompletionWidget)
        context.renderWidget(PowderGrindingTracker)
        context.renderWidget(TotalPowderWidget)
    }

    private fun GuiGraphics.renderWidget(
        widget: HudWidget
    ) {
        if (!widget.shouldRender()) return
        widget.render(this, 0, 0)
    }
}
