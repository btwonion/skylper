package dev.nyon.skylper.extensions.math

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3
import kotlin.math.sign

val NULL_VECTOR: Vec3Comparable = Vec3Comparable(0.0, 0.0, 0.0)

class Vec3Comparable(x: Double, y: Double, z: Double) : Vec3(x, y, z), Comparable<Vec3Comparable?> {
    constructor(source: Vec3) : this(source.x, source.y, source.z)

    constructor(source: BlockPos) : this(source.x.toDouble(), source.y.toDouble(), source.z.toDouble())

    override fun normalize(): Vec3Comparable {
        return Vec3Comparable(super.normalize())
    }

    override fun cross(vec: Vec3): Vec3 {
        return Vec3Comparable(super.cross(vec))
    }

    override fun subtract(vec: Vec3): Vec3 {
        return Vec3Comparable(super.subtract(vec))
    }

    override fun subtract(x: Double, y: Double, z: Double): Vec3Comparable {
        return Vec3Comparable(super.subtract(x, y, z))
    }

    override fun add(other: Vec3): Vec3Comparable {
        return Vec3Comparable(super.add(other))
    }

    override fun add(x: Double, y: Double, z: Double): Vec3Comparable {
        return Vec3Comparable(super.add(x, y, z))
    }

    override fun xRot(x: Float): Vec3Comparable {
        return Vec3Comparable(super.xRot(x))
    }

    override fun yRot(y: Float): Vec3Comparable {
        return Vec3Comparable(super.yRot(y))
    }

    override fun zRot(z: Float): Vec3Comparable {
        return Vec3Comparable(super.zRot(z))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vec3Comparable) return false
        return this.x == other.x && (this.y == other.y) && (this.z == other.z)
    }

    override fun hashCode(): Int {
        var bits = 1L
        bits = 31L * bits + x.doubleToLongBits()
        bits = 31L * bits + y.doubleToLongBits()
        bits = 31L * bits + z.doubleToLongBits()
        return (bits xor (bits shr 32)).toInt()
    }

    override fun compareTo(other: Vec3Comparable?): Int {
        if (other == null) return 0
        return if (this.y == other.y) {
            if (this.z == other.z) ((this.x - other.x).toInt())
            else ((this.z - other.z).toInt())
        }
        else ((this.y - other.y).toInt())
    }

    fun clone(): Vec3Comparable {
        return Vec3Comparable(x, y, z)
    }

    fun signEquals(other: Vec3): Boolean {
        return sign(x) == sign(other.x) && sign(y) == sign(other.y) && sign(z) == sign(other.z)
    }
}