package dev.nyon.skylper.extensions.render.hud

import dev.nyon.skylper.independentScope
import dev.nyon.skylper.minecraft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.seconds

interface HudWidget {
    companion object {
        const val W_PADDING = 5
        const val H_PADDING = 4

        val TITLE_HEIGHT = minecraft.font.lineHeight + 3 * H_PADDING
    }

    var x: Double
    var y: Double
    val height: Int
    val width: Int
    val title: Component

    /**
     * Renders the widget's background and title
     *
     * @return the next height which should be rendered
     */
    fun render(
        context: GuiGraphics, mouseX: Int, mouseY: Int
    ): Int {
        val xInt = x.toInt()
        val yInt = y.toInt()
        context.fill(xInt, yInt, xInt + width, yInt + height, 0x500C0C0C)

        context.drawString(
            minecraft.font,
            title.copy().withStyle { it.withBold(true).withColor(ChatFormatting.GOLD) },
            xInt + W_PADDING,
            yInt + H_PADDING,
            0xffffff
        )
        return yInt + TITLE_HEIGHT
    }

    fun clear()
    fun shouldRender(): Boolean

    fun update() {
        clear()
    }

    fun init() {
        independentScope.launch {
            while (true) {
                update()
                delay(.5.seconds)
            }
        }
    }
}
