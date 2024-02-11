package dev.nyon.skylper.skyblock.hollows.locations

import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.extensions.color
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
import dev.nyon.skylper.skyblock.hollows.Crystal
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint

object CrystalRunListener {
    private const val CRYSTAL_FOUND = "✦ CRYSTAL FOUND"
    private const val AMETHYST_CRYSTAL_INTERNAL_NAME = "internal_amethyst_crystal"
    private const val RUN_COMPLETED_MESSAGE = "You've earned a Crystal Loot Bundle!"

    private var nextIsCrystal = false

    fun init() = listenEvent<MessageEvent> { event ->
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = event.text.string
        rawMessage.checkFoundCrystal()
        rawMessage.checkRunCompleted()
    }

    private fun String.checkFoundCrystal() {
        if (nextIsCrystal) {
            val foundCrystal = Crystal.entries.find { contains(it.displayName) }
            nextIsCrystal = false
            if (foundCrystal != null) {
                playerData.currentProfile?.crystalHollows?.foundCrystals?.add(foundCrystal)
                val associatedStructure = foundCrystal.associatedStructure()
                HollowsModule.waypoints[if (associatedStructure == HollowsStructure.JUNGLE_TEMPLE) AMETHYST_CRYSTAL_INTERNAL_NAME else associatedStructure.internalWaypointName] = HollowsStructureWaypoint(
                    minecraft.player?.position() ?: return,
                    if (associatedStructure == HollowsStructure.JUNGLE_TEMPLE) "Amethyst Crystal" else associatedStructure.displayName,
                    minecraft.player?.position()?.y?.toInt() ?: return,
                    associatedStructure.waypointColor.color
                )
            }
        }
        if (contains(CRYSTAL_FOUND)) nextIsCrystal = true
    }

    private fun String.checkRunCompleted() {
        if (!contains(RUN_COMPLETED_MESSAGE)) return
        playerData.currentProfile?.crystalHollows?.foundCrystals?.clear()
    }
}