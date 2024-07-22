package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.Menu

object CompletionHighlighter {
    private val collectionsRegex = regex("menu.collections.collections")
    private val collectionsMaxedOut = regex("menu.collections.collectionsMaxedOut")
    private val collectionMaxedOut = regex("menu.collections.collectionMaxedOut")

    @Suppress("unused")
    val renderItemBackgroundEvent = listenEvent<RenderItemBackgroundEvent, Int?> {
        if (!config.menu.collections.highlightNonCompletedCollections) return@listenEvent null
        val screenName = title.string
        if (collectionsRegex.matches(screenName)) {
            val lore = slot.item.lore.map { it.string }
            if (lore.none { Menu.clickToViewRegex.matches(it) } || MinionCompletionHighlighter.craftedMinionsRegex.matches(slot.item.displayName.string)) return@listenEvent null
            if (lore.any { collectionsMaxedOut.matches("Collections Maxed Out: 100%") || collectionMaxedOut.matches("COLLECTION MAXED OUT!") }) return@listenEvent null
            return@listenEvent config.menu.collections.nonCompletedCollectionHighlightColor.rgb
        }

        null
    }
}
