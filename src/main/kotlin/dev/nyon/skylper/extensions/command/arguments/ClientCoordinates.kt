package dev.nyon.skylper.extensions.command.arguments

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

interface ClientCoordinates {
    fun getPosition(commandSourceStack: FabricClientCommandSource): Vec3

    fun getRotation(commandSourceStack: FabricClientCommandSource): Vec2

    fun getBlockPos(commandSourceStack: FabricClientCommandSource): BlockPos {
        return BlockPos.containing(this.getPosition(commandSourceStack))
    }

    val isXRelative: Boolean

    val isYRelative: Boolean

    val isZRelative: Boolean
}
