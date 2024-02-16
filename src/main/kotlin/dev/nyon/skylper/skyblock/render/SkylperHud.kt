package dev.nyon.skylper.skyblock.render

import de.hysky.skyblocker.skyblock.tabhud.widget.Widget
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderHudEvent
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.render.tabhud.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.hollows.render.tabhud.PowderGrindingWidget
import dev.nyon.skylper.skyblock.hollows.tracker.PowderGrindingTracker
import kotlinx.datetime.Clock
import net.minecraft.client.gui.GuiGraphics
import kotlin.time.Duration.Companion.minutes

object SkylperHud {
    fun init() {
        listenEvent<RenderHudEvent> {
            it.context.renderWidget(CrystalCompletionWidget) {
                HollowsModule.isPlayerInHollows && config.crystalHollows.crystalOverlay.enabled
            }

            it.context.renderWidget(PowderGrindingWidget) {
                val lastChestOpened = PowderGrindingTracker.lastChestOpened ?: return@renderWidget false
                HollowsModule.isPlayerInHollows && config.crystalHollows.powderGrindingOverlay.enabled && Clock.System.now() - lastChestOpened <= 5.minutes
            }
        }
    }

    private fun GuiGraphics.renderWidget(widget: Widget, condition: () -> Boolean) {
        if (!condition()) return
        widget.render(this)
    }
}