package dev.nyon.skylper.extensions.render.cooldown

import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.MiningCooldown
import kotlinx.datetime.Clock
import net.minecraft.world.item.ItemStack

object CooldownHandler {
    private val handlers = listOf<Cooldown>(MiningCooldown)

    fun getPercentage(stack: ItemStack): Float? {
        if (!PlayerSessionData.isOnSkyblock) return null
        val now = Clock.System.now()
        val matching = handlers.find { it.isCorrectItem(stack) } ?: return null
        val cooldownTime = matching.getCooldownTime()
        if (!matching.isEnabled() || matching.cooldownEnd == null || cooldownTime == null) return null

        val diff = matching.cooldownEnd!! - now
        val percentage = diff / cooldownTime
        return percentage.toFloat()
    }
}