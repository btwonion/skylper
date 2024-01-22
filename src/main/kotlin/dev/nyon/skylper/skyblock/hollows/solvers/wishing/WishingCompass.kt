package dev.nyon.skylper.skyblock.hollows.solvers.wishing

import dev.nyon.skylper.extensions.math.Line
import dev.nyon.skylper.extensions.math.Vec3Comparable
import dev.nyon.skylper.skyblock.hollows.solvers.wishing.ParticleHandler.MAX_PARTICLE_APPEAR_TIME
import kotlinx.datetime.Instant
import net.minecraft.core.BlockPos

private const val MAX_DISTANCE_BETWEEN_PARTICLES = 0.6
private const val MAX_DISTANCE_FROM_USE_TO_FIRST_PARTICLE = 9.0

data class Compass(
    var state: CompassState = CompassState.WAITING_FOR_FIRST_PARTICLE,
    var line: Line? = null,
    val pos: BlockPos,
    val used: Instant,
    var firstParticle: Vec3Comparable? = null,
    var previousParticle: Vec3Comparable? = null,
    var lastParticle: Vec3Comparable? = null,
    val processedParticles: MutableList<Pair<Vec3Comparable, Instant>> = mutableListOf()
) {
    val direction: Vec3Comparable?
        get() {
            return Vec3Comparable(
                firstParticle?.subtract(lastParticle?.clone()?.reverse() ?: return null)?.normalize() ?: return null
            )
        }

    fun directionTo(target: Vec3Comparable): Vec3Comparable? {
        return Vec3Comparable(firstParticle?.subtract(target.clone().reverse())?.normalize() ?: return null)
    }

    fun processParticle(x: Double, y: Double, z: Double, particleTime: Instant) {
        if (state == CompassState.FAILED_TIMEOUT_NO_REPEATING || state == CompassState.FAILED_TIMEOUT_NO_PARTICLES || state == CompassState.COMPLETED) return

        if (particleTime - this.used > MAX_PARTICLE_APPEAR_TIME) {
            state = CompassState.FAILED_TIMEOUT_NO_REPEATING
            return
        }

        val currentParticle = Vec3Comparable(x, y, z)
        if (state == CompassState.WAITING_FOR_FIRST_PARTICLE) {
            if (currentParticle.distanceTo(Vec3Comparable(pos)) < MAX_DISTANCE_FROM_USE_TO_FIRST_PARTICLE) {
                processedParticles.add(currentParticle to particleTime)
                firstParticle = currentParticle
                previousParticle = currentParticle
                state = CompassState.COMPUTING_LAST_PARTICLE
            }
            return
        }

        if (currentParticle.distanceTo(previousParticle ?: return) <= MAX_DISTANCE_BETWEEN_PARTICLES) {
            processedParticles.add(currentParticle to particleTime)
            previousParticle = currentParticle
            return
        }

        if (currentParticle.distanceTo(firstParticle ?: return) > MAX_DISTANCE_BETWEEN_PARTICLES) return

        processedParticles.add(currentParticle to particleTime)
        lastParticle = previousParticle
        line = Line(firstParticle!!, lastParticle!!)
        state = CompassState.COMPLETED
    }
}

enum class CompassState {
    WAITING_FOR_FIRST_PARTICLE,
    COMPUTING_LAST_PARTICLE,
    COMPLETED,
    FAILED_TIMEOUT_NO_REPEATING,
    FAILED_TIMEOUT_NO_PARTICLES,
}