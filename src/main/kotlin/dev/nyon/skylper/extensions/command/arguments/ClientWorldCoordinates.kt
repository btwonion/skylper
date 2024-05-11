package dev.nyon.skylper.extensions.command.arguments

import com.mojang.brigadier.StringReader
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.commands.arguments.coordinates.WorldCoordinate
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class ClientWorldCoordinates(
    private val x: WorldCoordinate, private val y: WorldCoordinate, private val z: WorldCoordinate
) : ClientCoordinates {
    override fun getPosition(commandSourceStack: FabricClientCommandSource): Vec3 {
        val vec3 = commandSourceStack.position
        return Vec3(x[vec3.x], y[vec3.y], z[vec3.z])
    }

    override fun getRotation(commandSourceStack: FabricClientCommandSource): Vec2 {
        val vec2 = commandSourceStack.rotation
        return Vec2(x[vec2.x.toDouble()].toFloat(), y[vec2.y.toDouble()].toFloat())
    }

    override val isXRelative: Boolean
        get() = x.isRelative

    override val isYRelative: Boolean
        get() = y.isRelative

    override val isZRelative: Boolean
        get() = z.isRelative

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other !is ClientWorldCoordinates) {
            false
        } else {
            if (x != other.x) {
                false
            } else {
                this.y == other.y && this.z == other.z
            }
        }
    }

    override fun hashCode(): Int {
        var i = x.hashCode()
        i = 31 * i + y.hashCode()
        return 31 * i + z.hashCode()
    }

    companion object {
        fun parseInt(stringReader: StringReader): ClientWorldCoordinates {
            val i = stringReader.cursor
            val worldCoordinate = WorldCoordinate.parseInt(stringReader)
            if (stringReader.canRead() && stringReader.peek() == ' ') {
                stringReader.skip()
                val worldCoordinate2 = WorldCoordinate.parseInt(stringReader)
                if (stringReader.canRead() && stringReader.peek() == ' ') {
                    stringReader.skip()
                    val worldCoordinate3 = WorldCoordinate.parseInt(stringReader)
                    return ClientWorldCoordinates(worldCoordinate, worldCoordinate2, worldCoordinate3)
                } else {
                    stringReader.cursor = i
                    throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader)
                }
            } else {
                stringReader.cursor = i
                throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader)
            }
        }
    }
}
