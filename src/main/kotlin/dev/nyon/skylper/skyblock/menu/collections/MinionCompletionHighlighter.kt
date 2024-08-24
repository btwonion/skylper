package dev.nyon.skylper.skyblock.menu.collections

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.lore
import dev.nyon.skylper.extensions.regex

object MinionCompletionHighlighter {
    val craftedMinionsRegex get() = regex("menu.collections.craftedMinions")
    private val clickToViewRecipesRegex get() = regex("menu.collections.clickToViewRecipes")
    private val minionUncompletedRegex get() = regex("menu.collections.minionUncompleted")

    @SkylperEvent
    fun renderItemBackgroundEvent(event: RenderItemBackgroundEvent): Int? {
        if (!config.menu.collections.highlightNonCompletedCollections) return null
        if (craftedMinionsRegex.matches(event.rawTitle)) {
            val lore = event.slot.item.lore.map { it.string }
            if (lore.none { clickToViewRecipesRegex.matches(it) }) return null
            if (lore.none { minionUncompletedRegex.matches(it) }) return null
            return config.menu.collections.nonCompletedCollectionHighlightColor.rgb
        }

        return null
    }
}
