package dev.nyon.skylper.skyblock.render

import dev.nyon.skylper.extensions.event.RenderAfterTranslucentEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.ChestHighlighter
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.ChestParticleHighlighter
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext

interface Highlighter {
    companion object {
        private val highlighters: List<Highlighter> = listOf(ChestHighlighter, ChestParticleHighlighter)

        @SkylperEvent
        fun renderInWorldEvent(event: RenderAfterTranslucentEvent) {
            highlighters.forEach { it.render(event.context) }
        }
    }

    fun render(context: WorldRenderContext)
}