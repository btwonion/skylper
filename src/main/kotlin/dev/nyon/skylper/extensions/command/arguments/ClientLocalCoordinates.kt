package dev.nyon.skylper.extensions.command.arguments

import com.mojang.brigadier.StringReader
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.commands.arguments.coordinates.WorldCoordinate
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import java.util.*

class ClientLocalCoordinates(private val left: Double, private val up: Double, private val forwards: Double) :
    ClientCoordinates {
    override fun getPosition(commandSourceStack: FabricClientCommandSource): Vec3 {
        val rotation = commandSourceStack.rotation
        val pos: Vec3 = ClientEntityAnchorArgument.Anchor.FEET.apply(commandSourceStack)
        val f = Mth.cos((rotation.y + 90.0f) * (Math.PI / 180.0).toFloat())
        val g = Mth.sin((rotation.y + 90.0f) * (Math.PI / 180.0).toFloat())
        val h = Mth.cos(-rotation.x * (Math.PI / 180.0).toFloat())
        val i = Mth.sin(-rotation.x * (Math.PI / 180.0).toFloat())
        val j = Mth.cos((-rotation.x + 90.0f) * (Math.PI / 180.0).toFloat())
        val k = Mth.sin((-rotation.x + 90.0f) * (Math.PI / 180.0).toFloat())
        val vec32 = Vec3((f * h).toDouble(), i.toDouble(), (g * h).toDouble())
        val vec33 = Vec3((f * j).toDouble(), k.toDouble(), (g * j).toDouble())
        val vec34 = vec32.cross(vec33).scale(-1.0)
        val d = vec32.x * this.forwards + vec33.x * this.up + vec34.x * this.left
        val e = vec32.y * this.forwards + vec33.y * this.up + vec34.y * this.left
        val l = vec32.z * this.forwards + vec33.z * this.up + vec34.z * this.left
        return Vec3(pos.x + d, pos.y + e, pos.z + l)
    }

    override fun getRotation(commandSourceStack: FabricClientCommandSource): Vec2 {
        return Vec2.ZERO
    }

    override val isXRelative: Boolean
        get() = true

    override val isYRelative: Boolean
        get() = true

    override val isZRelative: Boolean
        get() = true

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other !is ClientLocalCoordinates) {
            false
        } else {
            this.left == other.left && (this.up == other.up) && (this.forwards == other.forwards)
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(this.left, this.up, this.forwards)
    }

    companion object {
        fun parse(stringReader: StringReader): ClientLocalCoordinates {
            val i = stringReader.cursor
            val d = readDouble(stringReader, i)
            if (stringReader.canRead() && stringReader.peek() == ' ') {
                stringReader.skip()
                val e = readDouble(stringReader, i)
                if (stringReader.canRead() && stringReader.peek() == ' ') {
                    stringReader.skip()
                    val f = readDouble(stringReader, i)
                    return ClientLocalCoordinates(d, e, f)
                } else {
                    stringReader.cursor = i
                    throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader)
                }
            } else {
                stringReader.cursor = i
                throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader)
            }
        }

        private fun readDouble(stringReader: StringReader, i: Int): Double {
            if (!stringReader.canRead()) {
                throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext(stringReader)
            } else if (stringReader.peek() != '^') {
                stringReader.cursor = i
                throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader)
            } else {
                stringReader.skip()
                return if (stringReader.canRead() && stringReader.peek() != ' ') stringReader.readDouble() else 0.0
            }
        }
    }
}