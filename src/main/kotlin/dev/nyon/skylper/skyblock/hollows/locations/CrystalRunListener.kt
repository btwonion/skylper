package dev.nyon.skylper.skyblock.hollows.locations

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.Crystal
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import net.minecraft.network.chat.Component

object CrystalRunListener {
    private const val CRYSTAL_FOUND = "✦ CRYSTAL FOUND"
    private const val AMETHYST_CRYSTAL_INTERNAL_NAME = "internal_amethyst_crystal"
    private const val RUN_COMPLETED_MESSAGE = "You've earned a Crystal Loot Bundle!"
    private const val CRYSTAL_PLACED = "✦ You placed the "

    private var nextIsCrystal = false

    fun init() = listenEvent<MessageEvent> { event ->
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

                val associatedStructure = foundCrystal.associatedStructure()
                if (if (associatedStructure == HollowsStructure.JUNGLE_TEMPLE) config.mining.crystalHollows.hollowsWaypoints.amethystCrystal else associatedStructure.isWaypointEnabled()) HollowsModule.waypoints[if (associatedStructure == HollowsStructure.JUNGLE_TEMPLE) AMETHYST_CRYSTAL_INTERNAL_NAME else associatedStructure.internalWaypointName] =
                    Waypoint(
                        Component.literal(if (associatedStructure == HollowsStructure.JUNGLE_TEMPLE) "Amethyst Crystal" else associatedStructure.displayName),
                        minecraft.player?.position() ?: return,
                        WaypointType.BEAM,
                        associatedStructure.waypointColor
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