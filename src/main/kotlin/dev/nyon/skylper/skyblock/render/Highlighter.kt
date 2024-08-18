package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.RenderAfterTranslucentEvent
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.ChestHighlighter
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.ChestParticleHighlighter
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext

interface Highlighter {
    companion object {
        private val highlighters: List<Highlighter> = listOf(ChestHighlighter, ChestParticleHighlighter)

        fun init() {
            listenInfoEvent<RenderAfterTranslucentEvent> {
                highlighters.forEach { it.render(context) }
            }
        }
    }

    fun render(context: WorldRenderContext)
}