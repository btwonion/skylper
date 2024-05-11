package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule

object CrystalRunListener {
    private const val CRYSTAL_FOUND = "✦ CRYSTAL FOUND"
    private const val RUN_COMPLETED_MESSAGE = "You've earned a Crystal Loot Bundle!"
    private const val CRYSTAL_PLACED = "✦ You placed the "

    private var nextIsCrystal = false

    fun init() = listenEvent<MessageEvent, Unit> { event ->
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = event.text.string
        rawMessage.checkFoundCrystal()
        rawMessage.checkPlacedCrystal()
        rawMessage.checkRunCompleted()
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
        if (contains(CRYSTAL_FOUND)) nextIsCrystal = true
    }

    private fun String.checkPlacedCrystal() {
        if (!contains(CRYSTAL_PLACED)) return
        val crystal = drop(17).dropLast(9).run s@{ Crystal.entries.find { it.displayName == this@s } } ?: return
        EventHandler.invokeEvent(CrystalPlaceEvent(crystal))
    }

    private fun String.checkRunCompleted() {
        if (!contains(RUN_COMPLETED_MESSAGE)) return
        EventHandler.invokeEvent(NucleusRunCompleteEvent)
    }
}
