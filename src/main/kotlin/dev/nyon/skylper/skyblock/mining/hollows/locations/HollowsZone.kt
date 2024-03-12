package dev.nyon.skylper.skyblock.mining.hollows.locations

import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

enum class HollowsZone(val box: AABB) {
    CRYSTAL_NUCLEUS(AABB(462.0, 63.0, 461.0, 564.0, 181.0, 565.0)),
    JUNGLE(AABB(201.0, 63.0, 201.0, 513.0, 189.0, 513.0)),
    GOBLIN_HOLDOUT(AABB(201.0, 63.0, 512.0, 513.0, 189.0, 824.0)),
    MITHRIL_DEPOSITS(AABB(512.0, 63.0, 201.0, 824.0, 189.0, 513.0)),
    PRECURSOR_REMNANTS(AABB(512.0, 63.0, 512.0, 824.0, 189.0, 824.0)),
    MAGMA_FIELDS(AABB(201.0, 30.0, 201.0, 824.0, 64.0, 824.0))
}

val Vec3.hollowsZone: HollowsZone?
    get() {
        return HollowsZone.entries.firstOrNull { it.box.contains(this) }
    }