package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.models.mining.SkyMallPerk

object SkyMallApi {
    private val skyMallBeginningRegex get() = regex("chat.mining.skymall.beginning")
    var currentPerk: SkyMallPerk? = null

    @SkylperEvent
    fun messageEvent(event: MessageEvent) {
        if (!skyMallBeginningRegex.matches(event.rawText)) return
        currentPerk = SkyMallPerk.byMessage(event.rawText) ?: return
    }
}