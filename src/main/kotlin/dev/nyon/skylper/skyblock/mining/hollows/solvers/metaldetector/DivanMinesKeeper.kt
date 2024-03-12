package dev.nyon.skylper.skyblock.mining.hollows.solvers.metaldetector

import net.minecraft.world.phys.Vec3

enum class DivanMinesKeeper(val identifier: String, val offset: Vec3) {
    EMERALD("Emerald", Vec3(-3.0, 0.0, 33.0)),
    DIAMOND("Diamond", Vec3(33.0, 0.0, 3.0)),
    LAPIS("Lapis", Vec3(-33.0, 0.0, -3.0)),
    GOLD("Gold", Vec3(3.0, 0.0, -33.0))
}
