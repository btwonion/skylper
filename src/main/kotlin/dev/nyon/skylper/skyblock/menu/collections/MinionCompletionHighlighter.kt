package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore

object MinionCompletionHighlighter {
    @Suppress("unused")
    val renderItemBackgroundEvent = listenEvent<RenderItemBackgroundEvent, Int?> {
        if (!config.menu.collections.highlightNonCompletedCollections) return@listenEvent null
        val screenName = title.string
        if (screenName.contains("Crafted Minions")) {
            val lore = slot.item.lore.map { it.string }
            if (lore.none { it.contains("Click to view recipes!") }) return@listenEvent null
            val incompleteCount = lore.count { it.contains("✖") }
            if (incompleteCount == 0) return@listenEvent null
            if (incompleteCount == 1) return@listenEvent config.menu.collections.barelyCompletedCollectionHighlightColor.rgb
            if (lore.any { it.contains("✖") }) return@listenEvent config.menu.collections.nonCompletedCollectionHighlightColor.rgb
            return@listenEvent null
        }

        null
    }
}
