package dev.nyon.skylper.skyblock.hollows.locations

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.extensions.color
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import net.minecraft.world.phys.Vec3

@Suppress("SpellCheckingInspection")
object ChatStructureListener {
    private const val JUNGLE_TEMPLE_MESSAGE = "You are entering the jungle temple, your speed is reduced and your jump boost will not work!"
    private const val KING_YOLKAR_MESSAGE = "[NPC] King Yolkar"
    private const val PROFESSOR_ROBOT_MESSAGE = "[NPC] Professor Robot"

    fun init() = listenEvent<MessageEvent> { event ->
        if (!config.crystalHollows.showWaypoints) return@listenEvent
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = event.text.string
        val matchingStructure = when {
            rawMessage.contains(JUNGLE_TEMPLE_MESSAGE) -> HollowsStructure.JUNGLE_TEMPLE
            rawMessage.contains(KING_YOLKAR_MESSAGE) -> HollowsStructure.GOBLIN_KING
            rawMessage.contains(PROFESSOR_ROBOT_MESSAGE) -> HollowsStructure.PRECURSOR_CITY
            else -> null
        }

        if (matchingStructure != null) {
            val playerLoc = minecraft.player?.position() ?: return@listenEvent
            HollowsModule.waypoints[matchingStructure.internalWaypointName] = HollowsStructureWaypoint(
                Vec3(playerLoc.x, matchingStructure.minY.toDouble(), playerLoc.z),
                matchingStructure.displayName,
                if (matchingStructure == HollowsStructure.JUNGLE_TEMPLE) 115 else (matchingStructure.maxY + matchingStructure.minY) / 2,
                matchingStructure.waypointColor.color
            )
        }
    }
}