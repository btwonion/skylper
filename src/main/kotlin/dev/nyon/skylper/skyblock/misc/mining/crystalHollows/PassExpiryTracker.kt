package dev.nyon.skylper.skyblock.misc.mining.crystalHollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.models.Area

object PassExpiryTracker {
    private val regex get() = regex("chat.hollows.passExpire")

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun messageEvent(event: MessageEvent) {
        if (!regex.matches(event.rawText)) return
        if (!config.mining.crystalHollows.autoRenewPass) return
        minecraft.player?.connection?.sendCommand("purchasecrystallhollowspass")
    }
}
