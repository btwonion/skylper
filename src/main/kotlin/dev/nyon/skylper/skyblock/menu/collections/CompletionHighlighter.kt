package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore

object CompletionHighlighter {
    @Suppress("unused")
    val renderItemBackgroundEvent = listenEvent<RenderItemBackgroundEvent, Int?> { (title, slot) ->
        if (!config.menu.collections.highlightNonCompletedCollections) return@listenEvent null
        val screenName = title.string
        if (screenName.contains("Collections")) {
            val lore = slot.item.lore.map { it.string }
            if (lore.none { it.contains("Click to view!") }) return@listenEvent null
            if (lore.any { it.contains("Collections Maxed Out:") }) return@listenEvent null
            return@listenEvent config.menu.collections.nonCompletedCollectionHighlightColor.rgb
        }

        null
    }
}
