package dev.nyon.skylper.extensions.render.hud

import dev.nyon.skylper.extensions.render.hud.components.HudComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import kotlin.math.max

/**
 * A widget, which is split in x columns and x rows.
 * @param title The title of the widget
 * @param rows The rows the table should have.
 * @param columns The columns the table should have.
 */
abstract class TableHudWidget(override var title: Component, private val rows: Int, private val columns: Int) :
    HudWidget {
    val components: MutableMap<Int, HudComponent> = mutableMapOf()
    private val mutex = Mutex()
    private val rowHeight: Int
        get() = runBlocking { mutex.withLock { components.values.maxOfOrNull { it.height } ?: 0 } }
    private val columnWidths: Map<Int, Int>
        get() {
            val rows = runBlocking { mutex.withLock { components.values.chunked(columns) } }
            return buildMap {
                rows.forEach { row ->
                    row.forEachIndexed { index, hudComponent ->
                        val currentWidth = get(index)
                        if (currentWidth == null) {
                            put(index, hudComponent.width)
                            return@forEachIndexed
                        }

                        put(index, max(currentWidth, hudComponent.width))
                    }
                }
            }
        }

    override val height: Int
        get() {
            val paddingHeights = rows * HudWidget.H_PADDING
            val rowHeights = rows * rowHeight
            return paddingHeights + rowHeights + HudWidget.TITLE_HEIGHT
        }

    override val width: Int
        get() {
            val paddingWidths = (2 + (columns - 1) * 2) * HudWidget.W_PADDING
            val columnWidths = columnWidths.values.sum()
            return paddingWidths + columnWidths
        }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int): Int {
        if (columnWidths.size < columns - 1) return super.render(context, mouseX, mouseY)
        val xInt = x.toInt()
        var nextY = super.render(context, mouseX, mouseY)

        // Draw separators
        var separatorX = xInt + HudWidget.W_PADDING
        (0 ..< columns).drop(1).forEach {
            val columnWidth = columnWidths[it - 1] ?: 1
            separatorX += columnWidth + HudWidget.W_PADDING
            context.vLine(
                separatorX, nextY, nextY + rowHeight * rows + (rows - 1) * HudWidget.H_PADDING, 0x60FFFFFF
            )
            separatorX += HudWidget.W_PADDING
        }

        // Draw rows
        val rows = runBlocking { mutex.withLock { components.values.chunked(columns) } }
        rows.forEach { rowColumns ->
            var nextX = xInt + HudWidget.W_PADDING
            rowColumns.forEachIndexed { index, component ->
                component.render(context, nextX, nextY, mouseX, mouseY)
                val columnWidth = columnWidths[index] ?: component.width
                nextX += columnWidth + HudWidget.W_PADDING * 2 + 1
            }

            nextY += rowHeight + HudWidget.H_PADDING
        }

        return nextY
    }

    fun addComponent(row: Int, column: Int, component: HudComponent) {
        runBlocking { mutex.withLock { components[(row - 1) * columns + column] = component } }
    }

    override fun clear() {
        runBlocking {
            mutex.withLock {
                components.clear()
            }
        }
    }
}