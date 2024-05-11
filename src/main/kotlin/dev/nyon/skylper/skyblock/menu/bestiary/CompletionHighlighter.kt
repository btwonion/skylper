package dev.nyon.skylper.skyblock.menu.bestiary

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore

object CompletionHighlighter {
    @Suppress("unused")
    val renderItemBackgroundEvent = EventHandler.listenEvent<RenderItemBackgroundEvent, Int?> { (title, slot) ->
        if (!config.menu.bestiary.highlightNonCompletedBestiary) return@listenEvent null
        val screenName = title.string
        if (screenName.contains("Bestiary")) {
            val lore = slot.item.lore.map { it.string }
            val rest = screenName.drop(11)
            if (lore.none { it.contains("Click to view!") }) return@listenEvent null
            if (lore.any { (rest.isEmpty() && it.contains("Families Completed: 100%")) || it.contains("MAX!") }) return@listenEvent null

            return@listenEvent config.menu.bestiary.nonCompletedBestiaryHighlightColor.rgb
        }

        null
    }
}
