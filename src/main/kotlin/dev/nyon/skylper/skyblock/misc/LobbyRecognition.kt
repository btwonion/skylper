package dev.nyon.skylper.skyblock.misc

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.chatTranslatable
import dev.nyon.skylper.extensions.event.LevelChangeEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.SkyblockQuitEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.minecraft

object LobbyRecognition {
    private val joinedLobbies = mutableSetOf<String>()
    private var nextServer: String? = null

    private val regex get() = regex("chat.general.server_send")

    @SkylperEvent
    fun messageEvent(event: MessageEvent) {
        nextServer = regex.singleGroup(event.rawText) ?: return
    }

    @SkylperEvent
    fun skyblockQuitEvent(event: SkyblockQuitEvent) {
        nextServer = null
    }

    @SkylperEvent
    fun levelChangeEvent(event: LevelChangeEvent) {
        val server = nextServer ?: return
        val containsServerAlready = !joinedLobbies.add(server)
        nextServer = null

        if (!config.misc.recognizeLobbies) return
        if (!containsServerAlready) return
        minecraft.player?.sendSystemMessage(
            chatTranslatable("chat.skylper.misc.lobby_recognition")
        )
    }
}
