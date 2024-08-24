package dev.nyon.skylper.skyblock.menu.bestiary

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.Menu

object CompletionHighlighter {
    private val bestiaryRegex get() = regex("menu.bestiary.bestiary")
    private val familiesCompletedRegex get() = regex("menu.bestiary.familiesCompleted")
    private val maxedRegex get() = regex("menu.bestiary.maxed")

    @SkylperEvent
    fun renderItemBackgroundEvent(event: RenderItemBackgroundEvent): Int? {
        if (!config.menu.bestiary.highlightNonCompletedBestiary) return null
        if (bestiaryRegex.matches(event.rawTitle)) {
            val lore = event.slot.item.lore.map { it.string }
            if (lore.none { Menu.clickToViewRegex.matches(it) }) return null
            if (lore.any { familiesCompletedRegex.matches(it) || maxedRegex.matches(it) }) return null

            return config.menu.bestiary.nonCompletedBestiaryHighlightColor.rgb
        }

        return null
    }
}
