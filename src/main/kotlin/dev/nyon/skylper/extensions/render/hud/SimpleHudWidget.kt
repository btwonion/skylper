package dev.nyon.skylper.extensions.render.hud

import dev.nyon.skylper.extensions.render.hud.components.HudComponent
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

abstract class SimpleHudWidget(override val title: Component) : HudWidget {
    val components: MutableList<HudComponent> = mutableListOf()
    override val height: Int
        get() {
            val paddingHeights = components.size * HudWidget.H_PADDING
            val lineHeights = components.sumOf { it.height }
            return paddingHeights + lineHeights + HudWidget.TITLE_HEIGHT
        }
    override val width: Int
        get() = components.maxOf { it.width } + 2 * HudWidget.W_PADDING

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int): Int {
        val xInt = x.toInt()
        var nextY = super.render(context, mouseX, mouseY)

        components.forEach {
            it.render(context, xInt + HudWidget.W_PADDING, nextY, mouseX, mouseY)
            nextY += it.height + HudWidget.H_PADDING
        }

        return nextY
    }

    override fun clear() {
        components.clear()
    }
}