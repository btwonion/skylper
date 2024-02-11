package dev.nyon.skylper.skyblock.hollows.locations

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
    private const val KING_YOLKAR_MESSAGE = "§e[NPC] §6King Yolkar§f"
    private const val PROFESSOR_ROBOT_MESSAGE = "§e[NPC] Professor Robot§f"
    private const val JADE_KEEPERS_MESSAGE = "§e[NPC] §6Keeper of "
    private const val JUNGLE_TEMPLE_GUARDIAN_MESSAGE = "§e[NPC] §bKalhuiki Door Guardian§f"

    fun init() = listenEvent<MessageEvent> { event ->
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = event.text.string
        val matchingStructure = when {
            rawMessage.contains(KING_YOLKAR_MESSAGE) -> HollowsStructure.GOBLIN_KING
            rawMessage.contains(PROFESSOR_ROBOT_MESSAGE) -> HollowsStructure.PRECURSOR_CITY
            rawMessage.contains(JADE_KEEPERS_MESSAGE) -> HollowsStructure.MINES_OF_DIVAN
            rawMessage.contains(JUNGLE_TEMPLE_GUARDIAN_MESSAGE) -> HollowsStructure.JUNGLE_TEMPLE
            else -> null
        } ?: return@listenEvent

        if (HollowsModule.waypoints.containsKey(matchingStructure.internalWaypointName)) return@listenEvent
        val playerLoc = minecraft.player?.position() ?: return@listenEvent
        if (matchingStructure.isWaypointEnabled()) HollowsModule.waypoints[matchingStructure.internalWaypointName] = HollowsStructureWaypoint(
            Vec3(playerLoc.x, 0.0, playerLoc.z),
            matchingStructure.displayName,
            if (matchingStructure == HollowsStructure.JUNGLE_TEMPLE) 115 else (matchingStructure.maxY + matchingStructure.minY) / 2,
            matchingStructure.waypointColor.color
        )
    }
}