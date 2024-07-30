package dev.nyon.skylper.skyblock.menu.bestiary

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.Menu

object CompletionHighlighter {
    private val bestiaryRegex = regex("menu.bestiary.bestiary")
    private val familiesCompletedRegex = regex("menu.bestiary.familiesCompleted")
    private val maxedRegex = regex("menu.bestiary.maxed")

    @Suppress("unused")
    val renderItemBackgroundEvent = EventHandler.listenEvent<RenderItemBackgroundEvent, Int?> {
        if (!config.menu.bestiary.highlightNonCompletedBestiary) return@listenEvent null
        val screenName = title.string
        if (bestiaryRegex.matches(screenName)) {
            val lore = slot.item.lore.map { it.string }
            if (lore.none { Menu.clickToViewRegex.matches(it) }) return@listenEvent null
            if (lore.any { familiesCompletedRegex.matches(it) || maxedRegex.matches(it) }) return@listenEvent null

            return@listenEvent config.menu.bestiary.nonCompletedBestiaryHighlightColor.rgb
        }

        null
    }
}
