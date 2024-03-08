package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderHudEvent
import dev.nyon.skylper.extensions.render.hud.HudWidget
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.render.hud.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.hollows.render.hud.PowderGrindingWidget
import dev.nyon.skylper.skyblock.hollows.tracker.PowderGrindingTracker
import kotlinx.datetime.Clock
import net.minecraft.client.gui.GuiGraphics
import kotlin.time.Duration.Companion.minutes

object SkylperHud {
    fun init() {
        listenEvent<RenderHudEvent> {
            it.context.renderWidget(CrystalCompletionWidget) {
                HollowsModule.isPlayerInHollows && config.mining.crystalHollows.crystalOverlay.enabled
            }

            it.context.renderWidget(PowderGrindingWidget) {
                val lastChestOpened = PowderGrindingTracker.lastChestOpened ?: return@renderWidget false
                HollowsModule.isPlayerInHollows && config.mining.crystalHollows.powderGrindingOverlay.enabled && Clock.System.now() - lastChestOpened <= 5.minutes
            }
        }
    }

    private fun GuiGraphics.renderWidget(widget: HudWidget, condition: () -> Boolean) {
        if (!condition()) return
        widget.render(this, 0, 0)
    }
}