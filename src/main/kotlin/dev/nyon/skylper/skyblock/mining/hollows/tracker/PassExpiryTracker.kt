package dev.nyon.skylper.skyblock.mining.hollows.tracker

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule

object PassExpiryTracker {
    private val regex = regex("chat.hollows.passExpire")

    fun init() {
        listenEvent<MessageEvent, Unit> {
            if (!HollowsModule.isPlayerInHollows) return@listenEvent
            if (!regex.matches(rawText)) return@listenEvent
            if (!config.mining.crystalHollows.autoRenewPass) return@listenEvent
            minecraft.player?.connection?.sendCommand("purchasecrystallhollowspass")
        }
    }
}
