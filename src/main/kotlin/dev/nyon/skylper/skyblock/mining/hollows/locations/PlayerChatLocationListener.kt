package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.EventHandler
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

object PlayerChatLocationListener {
    private val regex = regex("chat.hollows.location")

    @Suppress("unused")
    private val messageEvent = listenEvent<MessageEvent, Unit> {
        if (!config.mining.crystalHollows.parseLocationChats) return@listenEvent
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

        val rawMessage = rawText.dropWhile { it != ':' }
        val loc = regex.groups(rawMessage).let {
            if (it.isEmpty()) return@let null

            return@let Vec3(it[2].toDoubleOrNull() ?: 0.0, it[3].toDoubleOrNull() ?: 35.0, it[4].toDoubleOrNull() ?: 0.0)
        } ?: return@listenEvent

        if (!HollowsModule.hollowsBox.contains(loc)) return@listenEvent

        handleRawLocation(loc, rawMessage)
    }

    private fun handleRawLocation(pos: Vec3, rawMessage: String) {
        if (config.mining.crystalHollows.automaticallyAddLocations) {
            val matchingSpecific = PreDefinedHollowsLocationSpecific.entries.find {
                rawMessage.contains(it.name, true) || rawMessage.contains(
                    it.key, true
                )
            }

            if (matchingSpecific != null) {
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

        val locationsComponent = PreDefinedHollowsLocationSpecific.entries.map { specific ->
            specific.displayName.copy().withStyle {
                it.withColor(ChatFormatting.RED)
                val command =
                    "/skylper hollows waypoints set ${specific.key} ${pos.x.toInt()} ${pos.y.toInt()} ${pos.z.toInt()}"
                it.withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))
            }
        }.fold(Component.empty()) { acc, new ->
            if (acc != Component.empty()) acc.append(Component.literal(", ").withStyle(ChatFormatting.WHITE))
            acc.append(new)
            acc
        }

        val finalComponent = Component.translatable("chat.skylper.hollows.locations.pick", locationsComponent)
        minecraft.player?.sendSystemMessage(finalComponent)
    }
}
