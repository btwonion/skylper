package dev.nyon.skylper.extensions.render.hud

import dev.nyon.skylper.extensions.render.hud.components.HudComponent
import dev.nyon.skylper.minecraft
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

abstract class SimpleHudWidget(override val title: Component) : HudWidget {
    val mutex = Mutex()
    val components: MutableList<HudComponent> = mutableListOf()
    override val height: Int
        get() {
            val paddingHeights = components.size * HudWidget.H_PADDING
            val lineHeights = runBlocking { mutex.withLock { components.sumOf { it.height } } }
            return paddingHeights + lineHeights + HudWidget.TITLE_HEIGHT
        }
    override val width: Int
        get() = (runBlocking { mutex.withLock { components.maxOfOrNull { it.width } } }
            ?: minecraft.font.width(title)) + 2 * HudWidget.W_PADDING

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int): Int {
        val xInt = x.toInt()
        var nextY = super.render(context, mouseX, mouseY)

        runBlocking {
            mutex.withLock {
                components.forEach {
                    it.render(context, xInt + HudWidget.W_PADDING, nextY, mouseX, mouseY)
                    nextY += it.height + HudWidget.H_PADDING
                }
            }
        }

        return nextY
    }

    override fun clear() {
        runBlocking {
            mutex.withLock {
                components.clear()
            }
        }
    }
}