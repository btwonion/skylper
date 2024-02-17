package dev.nyon.skylper.extensions

import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB

fun Player.radiusBox(radius: Int): AABB {
    val pos = this.position()
    return AABB(pos.x - radius, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius)
}