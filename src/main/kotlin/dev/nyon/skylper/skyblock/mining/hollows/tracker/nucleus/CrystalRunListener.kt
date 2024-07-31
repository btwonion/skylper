package dev.nyon.skylper.skyblock.mining.hollows.tracker.nucleus

import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import dev.nyon.skylper.skyblock.mining.hollows.locations.HollowsLocation

object CrystalRunListener {
    private val crystalFoundRegex = regex("chat.hollows.run.crystalFound")
    private val crystalPlacedRegex = regex("chat.hollows.run.crystalPlaced")
    private val runCompletedRegex = regex("chat.hollows.run.completed")

    fun init() = listenEvent<MessageEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        rawText.checkFoundCrystal()
        rawText.checkPlacedCrystal()
        rawText.checkRunCompleted()
    }

    private fun String.checkFoundCrystal() {
        val match = crystalFoundRegex.singleGroup(this) ?: return
        val foundCrystal = Crystal.entries.find { it.displayName == match } ?: return

        EventHandler.invokeEvent(CrystalFoundEvent(foundCrystal))

        val location = foundCrystal.associatedLocationSpecific()
        EventHandler.invokeEvent(
            LocatedHollowsStructureEvent(
                HollowsLocation(
                    minecraft.player!!.position(), location
                )
            )
        )
    }

    private fun String.checkPlacedCrystal() {
        val crystal =
            crystalPlacedRegex.singleGroup(this).run { Crystal.entries.find { it.displayName == this } } ?: return
        EventHandler.invokeEvent(CrystalPlaceEvent(crystal))
    }

    private fun String.checkRunCompleted() {
        if (!runCompletedRegex.matches(this)) return
        EventHandler.invokeEvent(NucleusRunCompleteEvent)
    }
}
