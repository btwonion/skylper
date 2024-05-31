package dev.nyon.skylper.skyblock.misc

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.extensions.SkyblockQuitEvent
import dev.nyon.skylper.minecraft
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

object LobbyRecognition {
    private val joinedLobbies = mutableSetOf<String>()
    private var nextServer: String? = null

    @Suppress("unused")
    val messageEvent = listenEvent<MessageEvent, Unit> {
        val raw = it.text.string
        if (!(raw.startsWith("Sending to server ") && raw.endsWith("..."))) return@listenEvent
        nextServer = raw.drop(18).dropLast(3)
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
