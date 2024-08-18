package dev.nyon.skylper.skyblock.menu.mining

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.RenderItemBackgroundEvent
import dev.nyon.skylper.extensions.rawLore
import dev.nyon.skylper.extensions.regex

object CommissionHighlighter {
    private val commissionsTitleRegex get() = regex("menu.commissions.title")
    private val commissionCompletedRegex get()  = regex("menu.commissions.completed")

    @Suppress("unused")
    val renderBackgroundEvent = listenEvent<RenderItemBackgroundEvent, Int?> {
        if (!config.mining.highlightCompletedCommissions) return@listenEvent null
        if (!commissionsTitleRegex.matches(rawTitle)) return@listenEvent null
        val item = slot.item
        if (item.rawLore.none { commissionCompletedRegex.matches(it) }) return@listenEvent null
        return@listenEvent config.mining.completedCommissionsHighlightColor.rgb
    }
}