package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.nameAsString
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.Menu

object CompletionHighlighter {
    private val collectionsRegex get() = regex("menu.collections.collections")
    private val collectionsMaxedOutRegex get() = regex("menu.collections.collectionsMaxedOut")
    private val collectionMaxedOutRegex get() = regex("menu.collections.collectionMaxedOut")

    @SkylperEvent
    fun renderItemBackgroundEvent(event: RenderItemBackgroundEvent): Int? {
        if (!config.menu.collections.highlightNonCompletedCollections) return null
        if (collectionsRegex.matches(event.rawTitle)) {
            val lore = event.slot.item.lore.map { it.string }
            if (lore.none { Menu.clickToViewRegex.matches(it) }) return null
            if (MinionCompletionHighlighter.craftedMinionsRegex.matches(event.slot.item.nameAsString)) return null
            if (lore.any { collectionsMaxedOutRegex.matches(it) || collectionMaxedOutRegex.matches(it) }) return null
            return config.menu.collections.nonCompletedCollectionHighlightColor.rgb
        }

        return null
    }
}
