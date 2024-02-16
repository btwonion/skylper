package dev.nyon.skylper.extensions

import de.hysky.skyblocker.utils.Utils
import net.minecraft.world.entity.LivingEntity

fun LivingEntity.hasMaxHealth(health: Float): Boolean {
    val actualHealth = if (Utils.getMayor() == "Derpy") health * 2 else health
    return maxHealth == actualHealth
}