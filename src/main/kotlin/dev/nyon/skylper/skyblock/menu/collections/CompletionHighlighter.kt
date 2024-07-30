package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.nameAsString
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.Menu

object CompletionHighlighter {
    private val collectionsRegex = regex("menu.collections.collections")
    private val collectionsMaxedOutRegex = regex("menu.collections.collectionsMaxedOut")
    private val collectionMaxedOutRegex = regex("menu.collections.collectionMaxedOut")

    @Suppress("unused")
    val renderItemBackgroundEvent = listenEvent<RenderItemBackgroundEvent, Int?> {
        if (!config.menu.collections.highlightNonCompletedCollections) return@listenEvent null
        val screenName = title.string
        if (collectionsRegex.matches(screenName)) {
            val lore = slot.item.lore.map { it.string }
            if (lore.none { Menu.clickToViewRegex.matches(it) }) return@listenEvent null
            if (MinionCompletionHighlighter.craftedMinionsRegex.matches(slot.item.nameAsString)) return@listenEvent null
            if (lore.any { collectionsMaxedOutRegex.matches(it) || collectionMaxedOutRegex.matches(it) }) return@listenEvent null
            return@listenEvent config.menu.collections.nonCompletedCollectionHighlightColor.rgb
        }

        null
    }
}
