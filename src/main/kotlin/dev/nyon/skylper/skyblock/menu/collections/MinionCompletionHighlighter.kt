package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.regex

object MinionCompletionHighlighter {
    val craftedMinionsRegex = regex("menu.collections.craftedMinions")
    private val clickToViewRecipesRegex = regex("menu.collections.clickToViewRecipes")
    private val minionUncompletedRegex = regex("menu.collections.minionUncompleted")

    @Suppress("unused")
    val renderItemBackgroundEvent = listenEvent<RenderItemBackgroundEvent, Int?> {
        if (!config.menu.collections.highlightNonCompletedCollections) return@listenEvent null
        val screenName = title.string
        if (craftedMinionsRegex.matches(screenName)) {
            val lore = slot.item.lore.map { it.string }
            if (lore.none { clickToViewRecipesRegex.matches(it) }) return@listenEvent null
            val incompleteCount = lore.count { minionUncompletedRegex.matches(it) }
            if (incompleteCount == 0) return@listenEvent null
            if (incompleteCount == 1) return@listenEvent config.menu.collections.barelyCompletedCollectionHighlightColor.rgb
            if (lore.any { minionUncompletedRegex.matches(it) }) return@listenEvent config.menu.collections.nonCompletedCollectionHighlightColor.rgb
            return@listenEvent null
        }

        null
    }
}
