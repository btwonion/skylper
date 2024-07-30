package dev.nyon.skylper.skyblock.misc

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.LevelChangeEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.SkyblockQuitEvent
import dev.nyon.skylper.minecraft
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

object LobbyRecognition {
    private val joinedLobbies = mutableSetOf<String>()
    private var nextServer: String? = null

    private val regex = regex("chat.general.server_send")

    @Suppress("unused")
    val messageEvent = listenEvent<MessageEvent, Unit> {
        nextServer = regex.singleGroup(rawText) ?: return@listenEvent
    }

    @Suppress("unused")
    val skyblockQuitEvent = listenEvent<SkyblockQuitEvent, Unit> {
        nextServer = null
    }

    @Suppress("unused")
    val levelChangeEvent = listenEvent<LevelChangeEvent, Unit> {
        val server = nextServer ?: return@listenEvent
        val containsServerAlready = !joinedLobbies.add(server)
        nextServer = null

        if (!config.misc.recognizeLobbies) return@listenEvent
        if (!containsServerAlready) return@listenEvent
        minecraft.player?.sendSystemMessage(Component.translatable("chat.skylper.misc.lobby_recognition").withStyle(ChatFormatting.GRAY))
    }
}
