package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.models.mining.SkyMallPerk

object SkyMallApi {
    private val skyMallBeginningRegex get() = regex("chat.mining.skymall.beginning")
    var currentPerk: SkyMallPerk? = null

    @Suppress("unused")
    private val messageEvent = listenInfoEvent<MessageEvent> {
        if (!skyMallBeginningRegex.matches(rawText)) return@listenInfoEvent
        currentPerk = SkyMallPerk.byMessage(rawText) ?: return@listenInfoEvent
    }
}