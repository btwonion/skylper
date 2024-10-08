package dev.nyon.skylper.skyblock.cooldowns

import kotlinx.datetime.Instant
import net.minecraft.world.item.ItemStack
import kotlin.time.Duration

interface Cooldown {
    var cooldownEnd: Instant?

    fun isEnabled(): Boolean

    fun isCorrectItem(stack: ItemStack): Boolean

    fun getCooldownTime(): Duration?
}
