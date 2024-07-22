package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule

object CrystalRunListener {
    private val crystalFoundRegex = regex("chat.hollows.run.crystalFound")
    private val crystalPlacedRegex = regex("chat.hollows.run.crystalPlaced")  // todo
    private val runCompletedRegex = regex("chat.hollows.run.completed")

    private var nextIsCrystal = false

    fun init() = listenEvent<MessageEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        rawText.checkFoundCrystal()
        rawText.checkPlacedCrystal()
        rawText.checkRunCompleted()
    }

    private fun String.checkFoundCrystal() {
        if (nextIsCrystal) {
            val foundCrystal = Crystal.entries.find { contains(it.displayName) }
            nextIsCrystal = false
            if (foundCrystal != null) {
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
        }
        if (crystalFoundRegex.matches(this)) nextIsCrystal = true
    }

    private fun String.checkPlacedCrystal() {
        val crystal = crystalPlacedRegex.singleGroup(this).run { Crystal.entries.find { it.displayName == this } } ?: return
        EventHandler.invokeEvent(CrystalPlaceEvent(crystal))
    }

    private fun String.checkRunCompleted() {
        if (!runCompletedRegex.matches(this)) return
        EventHandler.invokeEvent(NucleusRunCompleteEvent)
    }
}
