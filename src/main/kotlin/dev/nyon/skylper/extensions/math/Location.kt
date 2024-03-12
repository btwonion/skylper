package dev.nyon.skylper.extensions.math

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

val Vec3.blockPos: BlockPos
    get() {
        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }
