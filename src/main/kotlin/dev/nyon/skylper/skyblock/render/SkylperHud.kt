package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderHudEvent
import dev.nyon.skylper.extensions.render.hud.HudWidget
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import dev.nyon.skylper.skyblock.mining.hollows.render.hud.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.mining.hollows.tracker.powder.PowderGrindingWidget
import dev.nyon.skylper.skyblock.mining.hollows.tracker.powder.PowderGrindingTracker
import net.minecraft.client.gui.GuiGraphics

object SkylperHud {
    fun init() {
        listenEvent<RenderHudEvent> {
            it.context.renderWidget(CrystalCompletionWidget) {
                HollowsModule.isPlayerInHollows && config.mining.crystalHollows.crystalOverlay.enabled
            }

            it.context.renderWidget(PowderGrindingWidget) {
                HollowsModule.isPlayerInHollows && config.mining.crystalHollows.powderGrindingOverlay.enabled && PowderGrindingTracker.isGrinding
            }
        }
    }

    private fun GuiGraphics.renderWidget(widget: HudWidget, condition: () -> Boolean) {
        if (!condition()) return
        widget.render(this, 0, 0)
    }
}