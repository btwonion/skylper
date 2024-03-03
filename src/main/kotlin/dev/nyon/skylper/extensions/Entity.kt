package dev.nyon.skylper.extensions

import dev.nyon.skylper.skyblock.data.online.Mayor
import dev.nyon.skylper.skyblock.data.online.MayorData
import net.minecraft.world.entity.LivingEntity

fun LivingEntity.hasMaxHealth(health: Float): Boolean {
    val actualHealth = if (MayorData.mayor == Mayor.DERPY) health * 2 else health
    return maxHealth == actualHealth
}