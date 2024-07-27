package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderHudEvent
import dev.nyon.skylper.extensions.render.hud.HudWidget
import dev.nyon.skylper.skyblock.data.online.IslandGroups
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.TotalPowderWidget
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import dev.nyon.skylper.skyblock.mining.hollows.tracker.nucleus.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.mining.hollows.tracker.powder.PowderGrindingTracker
import net.minecraft.client.gui.GuiGraphics

object SkylperHud {
    fun init() {
        listenEvent<RenderHudEvent, Unit> {
            context.renderWidget(CrystalCompletionWidget) {
                HollowsModule.isPlayerInHollows && config.mining.crystalHollows.crystalOverlay.enabled
            }

            context.renderWidget(PowderGrindingTracker) {
                HollowsModule.isPlayerInHollows && config.mining.crystalHollows.powderGrindingOverlay.enabled && PowderGrindingTracker.isGrinding
            }

            context.renderWidget(TotalPowderWidget) {
                IslandGroups.groups.mining.contains(PlayerSessionData.currentArea) && config.mining.totalPowderOverlay.enabled
            }
        }
    }

    private fun GuiGraphics.renderWidget(
        widget: HudWidget, condition: () -> Boolean
    ) {
        if (!condition()) return
        widget.render(this, 0, 0)
    }
}
