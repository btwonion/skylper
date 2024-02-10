package dev.nyon.skylper.skyblock.hollows

import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent

object CrystalRunListener {

    fun listenChat() = listenEvent<MessageEvent> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent

    }
}