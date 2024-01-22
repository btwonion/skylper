package dev.nyon.skylper.skyblock.hollows

import net.minecraft.world.phys.Vec3

val Vec3.hollowsZone: HollowsZone?
    get() {
        return HollowsZone.entries.firstOrNull { it.box.contains(this) }
    }