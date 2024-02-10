package dev.nyon.skylper.extensions.math

import dev.nyon.skylper.extensions.x
import dev.nyon.skylper.extensions.y
import dev.nyon.skylper.extensions.z
import net.minecraft.commands.arguments.coordinates.Coordinates
import net.minecraft.commands.arguments.coordinates.WorldCoordinates
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

val Vec3.blockPos: BlockPos
    get() {
        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }

fun Coordinates.blockPos(playerLoc: Vec3): BlockPos? {
    val worldCord = this as? WorldCoordinates ?: return null

    val pos = BlockPos(
        worldCord.x.get(playerLoc.x).toInt(), worldCord.y.get(playerLoc.y).toInt(), worldCord.z.get(playerLoc.z).toInt()
    )
    return pos
}

fun Coordinates.vec3(playerLoc: Vec3): Vec3? {
    val worldCord = this as? WorldCoordinates ?: return null

    val pos = Vec3(worldCord.x.get(playerLoc.x), worldCord.y.get(playerLoc.y), worldCord.z.get(playerLoc.z))
    return pos
}