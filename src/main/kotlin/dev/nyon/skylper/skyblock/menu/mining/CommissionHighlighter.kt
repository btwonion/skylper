package dev.nyon.skylper.skyblock.menu.mining

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.rawLore
import dev.nyon.skylper.extensions.regex

object CommissionHighlighter {
    private val commissionsTitleRegex get() = regex("menu.commissions.title")
    private val commissionCompletedRegex get() = regex("menu.commissions.completed")

    @SkylperEvent
    fun renderBackgroundEvent(event: RenderItemBackgroundEvent): Int? {
        if (!config.mining.highlightCompletedCommissions) return null
        if (!commissionsTitleRegex.matches(event.rawTitle)) return null
        val item = event.slot.item
        if (item.rawLore.none { commissionCompletedRegex.matches(it) }) return null
        return config.mining.completedCommissionsHighlightColor.rgb
    }
}