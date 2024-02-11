package dev.nyon.skylper.skyblock.render

import de.hysky.skyblocker.skyblock.tabhud.widget.Widget
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderHudEvent
import dev.nyon.skylper.skyblock.hollows.Crystal
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.render.tabhud.CrystalCompletionWidget
import net.minecraft.client.gui.GuiGraphics

object SkylperHud {
    fun init() {
        listenEvent<RenderHudEvent> {
            it.context.renderWidget(CrystalCompletionWidget) {
                HollowsModule.isPlayerInHollows && config.crystalHollows.crystalOverlay.enabled
            }
        }
    }

    private fun GuiGraphics.renderWidget(widget: Widget, condition: () -> Boolean) {
        if (!condition()) return
        widget.render(this)
    }
}