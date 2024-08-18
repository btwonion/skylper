package dev.nyon.skylper.skyblock.misc.mining.crystalHollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi

object PassExpiryTracker {
    private val regex get() = regex("chat.hollows.passExpire")

    @Suppress("unused")
    private val messageEvent = listenInfoEvent<MessageEvent> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent
        if (!regex.matches(rawText)) return@listenInfoEvent
        if (!config.mining.crystalHollows.autoRenewPass) return@listenInfoEvent
        minecraft.player?.connection?.sendCommand("purchasecrystallhollowspass")
    }
}
