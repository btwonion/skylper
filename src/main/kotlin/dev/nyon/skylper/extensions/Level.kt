package dev.nyon.skylper.extensions

import dev.nyon.skylper.minecraft
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import kotlin.reflect.KClass

fun Level.findArmorStandsWithName(name: String): List<ArmorStand> {
    return entityInRadius(ArmorStand::class, name).map { it as ArmorStand }
}

fun Level.entityInRadius(clazz: KClass<out Entity>, name: String, radius: Int = 50): List<LivingEntity> {
    return getEntitiesOfClass(
        clazz.java, minecraft.player?.radiusBox(radius) ?: return emptyList()
    ).filter { it.isAlive && it.customName?.string?.contains(name) == true }.map { it as LivingEntity }
}