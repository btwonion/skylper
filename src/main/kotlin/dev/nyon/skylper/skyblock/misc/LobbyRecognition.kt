package dev.nyon.skylper.skyblock.misc

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.LevelChangeEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.SkyblockQuitEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.minecraft
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

object LobbyRecognition {
    private val joinedLobbies = mutableSetOf<String>()
    private var nextServer: String? = null

    private val regex get() = regex("chat.general.server_send")

    @Suppress("unused")
    val messageEvent = listenInfoEvent<MessageEvent> {
        nextServer = regex.singleGroup(rawText) ?: return@listenInfoEvent
    }

    @Suppress("unused")
    val skyblockQuitEvent = listenInfoEvent<SkyblockQuitEvent> {
        nextServer = null
    }

    @Suppress("unused")
    val levelChangeEvent = listenInfoEvent<LevelChangeEvent> {
        val server = nextServer ?: return@listenInfoEvent
        val containsServerAlready = !joinedLobbies.add(server)
        nextServer = null

        if (!config.misc.recognizeLobbies) return@listenInfoEvent
        if (!containsServerAlready) return@listenInfoEvent
        minecraft.player?.sendSystemMessage(
            Component.translatable("chat.skylper.misc.lobby_recognition").withStyle(ChatFormatting.GRAY)
        )
    }
}
