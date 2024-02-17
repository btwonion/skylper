package dev.nyon.skylper.skyblock.hollows.tracker

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsModule

object PassExpiryTracker {
    private const val EXPIRE_MESSAGE = "Your pass to the Crystal Hollows will expire in 1 minute"

    fun init() {
        listenEvent<MessageEvent> {
            if (!HollowsModule.isPlayerInHollows) return@listenEvent
            if (!it.text.string.contains(EXPIRE_MESSAGE)) return@listenEvent
            if (!config.crystalHollows.autoRenewPass) return@listenEvent
            minecraft.player?.connection?.sendCommand("purchasecrystallhollowspass")
        }
    }
}