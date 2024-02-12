package dev.nyon.skylper.extensions

import net.minecraft.world.entity.LivingEntity

// TODO derpy
fun LivingEntity.hasMaxHealth(health: Float): Boolean {
    return maxHealth == health
}