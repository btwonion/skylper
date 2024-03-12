package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

object PlayerChatLocationListener {
    private val regex =
        "^.*?(?<Num>-?\\d{1,3})(?:[,\\s]*(?<Num2>-?\\d{1,3}))?(?:[,\\s]*(?<Num3>-?\\d{1,3}))?.*?\$".toRegex()

    data class RawLocation(val x: Double, val y: Double?, val z: Double)

    @Suppress("unused")
    private val messageEvent = listenEvent<MessageEvent, Unit> { event ->
        if (!config.mining.crystalHollows.parseLocationChats) return@listenEvent
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = event.text.string.dropWhile { it != ':' }
        val loc = regex.find(rawMessage)?.groupValues?.toMutableList()?.also { values ->
            values.removeAll { it.length > 3 || it.isEmpty() || it.toDoubleOrNull() == null }
        }?.toList()?.let {
            when (it.size) {
                2 -> RawLocation(it[0].toDouble(), null, it[1].toDouble())
                3 -> RawLocation(it[0].toDouble(), it[1].toDouble(), it[2].toDouble())
                else -> return@listenEvent
            }
        } ?: return@listenEvent

        if (!HollowsModule.hollowsBox.contains(loc.x, loc.y ?: 35.0, loc.z)) return@listenEvent

        handleRawLocation(loc, rawMessage)
    }

    private fun handleRawLocation(location: RawLocation, rawMessage: String) {
        if (config.mining.crystalHollows.automaticallyAddLocations) {
            val matchingSpecific = PreDefinedHollowsLocationSpecific.entries.find {
                rawMessage.contains(it.name, true) || rawMessage.contains(
                    it.key, true
                )
            }

            if (matchingSpecific != null) {
                val pos = Vec3(location.x, location.y ?: 80.0, location.z)
                val hollowsLocation = HollowsLocation(pos, matchingSpecific)
                EventHandler.invokeEvent(LocatedHollowsStructureEvent(hollowsLocation))

                minecraft.player?.sendSystemMessage(
                    Component.translatable(
                        "chat.skylper.hollows.locations.found", matchingSpecific.displayName.string, pos.x, pos.y, pos.z
                    )
                )
                return
            }
        }

        PreDefinedHollowsLocationSpecific.entries.forEach { specific ->
            minecraft.player?.sendSystemMessage(Component.translatable(
                "chat.skylper.hollows.locations.pick", specific.displayName.copy().withStyle(ChatFormatting.RED)
            ).withStyle {
                val command =
                    "/skylper hollows waypoints set ${specific.key} ${location.x.toInt()} ${location.y?.toInt() ?: 80} ${location.z.toInt()}"
                it.withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))
            })
        }
    }
}