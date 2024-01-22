package dev.nyon.skylper.extensions.math

import net.minecraft.world.phys.Vec3
import kotlin.math.abs

private const val DOUBLE_EPSILON: Double = 4.9E-324

@Suppress("SpellCheckingInspection")
class Line(private val point1: Vec3, private val point2: Vec3) {
    fun getMidpoint(): Vec3 {
        return Vec3(
            (point1.x + point2.x) / 2, (point1.y + point2.y) / 2, (point1.z + point2.z) / 2
        )
    }

    fun getIntersectionLineSegment(other: Line): Line? {
        val p13 = point1.subtract(other.point1)
        val p43 = other.point2.subtract(other.point1)

        val p21 = point2.subtract(point1)
        if (p43.dot(p43) < DOUBLE_EPSILON || p21.dot(p21) < DOUBLE_EPSILON) return null

        val d1343: Double = p13.x * p43.x + p13.y * p43.y + p13.z * p43.z
        val d4321: Double = p43.x * p21.x + p43.y * p21.y + p43.z * p21.z
        val d1321: Double = p13.x * p21.x + p13.y * p21.y + p13.z * p21.z
        val d4343: Double = p43.x * p43.x + p43.y * p43.y + p43.z * p43.z
        val d2121: Double = p21.x * p21.x + p21.y * p21.y + p21.z * p21.z

        val denom = d2121 * d4343 - d4321 * d4321
        if (abs(denom) < DOUBLE_EPSILON) return null

        val numer = d1343 * d4321 - d1321 * d4343

        val mua = numer / denom
        val mub = (d1343 + d4321 * (mua)) / d4343

        val resultSegment = Line(
            Vec3(
                ((point1.x + mua * p21.x).toFloat()).toDouble(),
                ((point1.y + mua * p21.y).toFloat()).toDouble(),
                ((point1.z + mua * p21.z).toFloat()).toDouble()
            ), Vec3(
                ((other.point1.x + mub * p43.x).toFloat()).toDouble(),
                ((other.point1.y + mub * p43.y).toFloat()).toDouble(),
                ((other.point1.z + mub * p43.z).toFloat()).toDouble()
            )
        )

        return resultSegment
    }
}