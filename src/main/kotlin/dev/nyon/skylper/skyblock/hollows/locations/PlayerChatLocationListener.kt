package dev.nyon.skylper.skyblock.hollows.locations

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

object PlayerChatLocationListener {
    private val regex = "^.*?(?<Num>-?\\d{1,3})(?:[,\\s]*(?<Num2>-?\\d{1,3}))?(?:[,\\s]*(?<Num3>-?\\d{1,3}))?.*?\$".toRegex()

    data class RawLocation(val x: Double, val y: Double?, val z: Double)

    fun init() = listenEvent<MessageEvent> { event ->
        if (!config.crystalHollows.parseLocationChats) return@listenEvent
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = event.text.string
        val loc = regex.find(rawMessage)?.groupValues?.toMutableList()
            .also { set -> set?.removeIf { it.isEmpty() || it.toDoubleOrNull() == null } }
            ?.let {
                when (it.size) {
                    2 -> RawLocation(it[0].toDouble(), null, it[1].toDouble())
                    3 -> RawLocation(it[0].toDouble(), it[1].toDouble(), it[2].toDouble())
                    else -> return@listenEvent
                }
            } ?: return@listenEvent

        if (!HollowsModule.hollowsBox.contains(
                loc.x, loc.y ?: 30.0, loc.z
            )) return@listenEvent

        handleRawLocation(loc, rawMessage)
    }

    private fun handleRawLocation(location: RawLocation, rawMessage: String) {
        val matchingStructure = HollowsStructure.entries.find { rawMessage.contains(it.displayName, true) }
        if (HollowsModule.waypoints.containsKey(matchingStructure?.internalWaypointName)) return
        if (matchingStructure != null) {
            val pos = Vec3(
                location.x, location.y ?: matchingStructure.minY.toDouble(), location.z
            )
            HollowsModule.waypoints[matchingStructure.internalWaypointName] = HollowsStructureWaypoint(
                pos, matchingStructure
            )
            minecraft.player?.sendSystemMessage(
                Component.translatable(
                    "chat.skylper.hollows.locations.found", matchingStructure.displayName, pos.x, pos.y, pos.z
                )
            )
            return
        }

        val possibleStructures = HollowsStructure.entries.filterNot { HollowsModule.waypoints.containsKey(it.internalWaypointName) }
        possibleStructures.forEach { structure ->
            minecraft.player?.sendSystemMessage(Component.translatable(
                "chat.skylper.hollows.locations.pick",
                Component.literal(structure.displayName).withStyle(ChatFormatting.RED)
            ).withStyle {
                val command = "/skylper hollows waypoints set ${structure.internalWaypointName} ${location.x.toInt()} ${location.y?.toInt() ?: structure.minY} ${location.z.toInt()}"
                it.withClickEvent(
                    ClickEvent(
                        ClickEvent.Action.SUGGEST_COMMAND, command
                    )
                )
            })
        }
    }
}