@file:Suppress("unused")

package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent

object PlayerDataUpdater {
    fun initUpdaters() {
        profileUpdateChecker()
    }

    private fun profileUpdateChecker() {
        listenInfoEvent<ProfileChangeEvent> {
            if (playerData.profiles.containsKey(next)) return@listenInfoEvent
            playerData.profiles[next ?: return@listenInfoEvent] = ProfileData()
        }
    }

    private val tablistMithrilPowderRegex get() = regex("tablist.mining.mithril")
    private val tablistGemstonePowderRegex get() = regex("tablist.mining.gemstone")
    private val tablistGlacitePowderRegex get() = regex("tablist.mining.glacite")

    private val tablistUpdateEvent = listenInfoEvent<TablistUpdateEvent> {
        parsePowder(cleanLines)
    }

    private val sideboardUpdateEvent = listenInfoEvent<SideboardUpdateEvent> {
        parsePowder(cleanLines)
    }

    private fun parsePowder(lines: List<String>) {
        lines.forEach { line ->
            val mithrilPowder = tablistMithrilPowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val gemstonePowder = tablistGemstonePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val glacitePowder = tablistGlacitePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()

            if (mithrilPowder != null) {
                currentProfile.heartOfTheMountain.currentGlacitePowder = mithrilPowder
                EventHandler.invokeEvent(PowderUpdateEvent(PowderUpdateEvent.PowderType.MITHRIL, mithrilPowder))
            }
            if (gemstonePowder != null) {
                currentProfile.heartOfTheMountain.currentGlacitePowder = gemstonePowder
                EventHandler.invokeEvent(PowderUpdateEvent(PowderUpdateEvent.PowderType.GEMSTONE, gemstonePowder))
            }
            if (glacitePowder != null) {
                currentProfile.heartOfTheMountain.currentGlacitePowder = glacitePowder
                EventHandler.invokeEvent(PowderUpdateEvent(PowderUpdateEvent.PowderType.GLACITE, glacitePowder))
            }
        }
    }
}
