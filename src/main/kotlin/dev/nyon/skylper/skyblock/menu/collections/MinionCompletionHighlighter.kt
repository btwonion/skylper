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
            if (lore.none { minionUncompletedRegex.matches(it) }) return@listenEvent null
            return@listenEvent config.menu.collections.nonCompletedCollectionHighlightColor.rgb
        }

        null
    }
}
