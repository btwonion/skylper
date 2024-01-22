package dev.nyon.skylper.skyblock.hollows.solvers.wishing

import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.ParticleSpawnEvent
import dev.nyon.skylper.extensions.math.Vec3Comparable
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

object ParticleHandler {
    val MAX_PARTICLE_APPEAR_TIME = 5000.milliseconds

    fun initParticleListener() {
        listenEvent<ParticleSpawnEvent> {
            if (it.options.type !is SimpleParticleType) return@listenEvent
            onParticleSpawn(
                it.options.type as SimpleParticleType, it.pos.x, it.pos.y, it.pos.z
            )
        }
    }

    private fun onParticleSpawn(type: SimpleParticleType, x: Double, y: Double, z: Double) {
        if (type != ParticleTypes.HAPPY_VILLAGER || !HollowsModule.isPlayerInHollows) return

        val spawnTime = Clock.System.now()

        if (WishingCompassSolver.firstCompass != null && WishingCompassSolver.state != WishingCompassSolver.WishingCompassSolverState.SOLVED && spawnTime < WishingCompassSolver.firstCompass!!.used + 2.minutes) WishingCompassSolver.seenParticles.add(
            Vec3Comparable(
                x, y, z
            ) to spawnTime
        )

        processParticles(x, y, z, spawnTime)
        val (solutionPos, possibleStructures) = WishingCompassSolver.processBothCompasses() ?: return

        ResultHandler.handleSolveResult(solutionPos, possibleStructures)
    }

    private fun processParticles(
        x: Double, y: Double, z: Double, currentTime: Instant
    ) {
        val currentCompass = when (WishingCompassSolver.state) {
            WishingCompassSolver.WishingCompassSolverState.PROCESSING_FIRST_USE -> WishingCompassSolver.firstCompass
            WishingCompassSolver.WishingCompassSolverState.NEED_SECOND_COMPASS -> WishingCompassSolver.secondCompass
            else -> return
        } ?: return

        currentCompass.processParticle(x, y, z, currentTime)

        when (currentCompass.state) {
            CompassState.FAILED_TIMEOUT_NO_PARTICLES -> {
                WishingCompassSolver.state = WishingCompassSolver.WishingCompassSolverState.FAILED_TIMEOUT_NO_PARTICLES
                return
            }
            CompassState.FAILED_TIMEOUT_NO_REPEATING -> {
                WishingCompassSolver.state = WishingCompassSolver.WishingCompassSolverState.FAILED_TIMEOUT_NO_REPEATING
                return
            }
            CompassState.WAITING_FOR_FIRST_PARTICLE, CompassState.COMPUTING_LAST_PARTICLE -> return
            CompassState.COMPLETED -> {
                if (WishingCompassSolver.state == WishingCompassSolver.WishingCompassSolverState.NEED_SECOND_COMPASS) return
                if (WishingCompassSolver.state == WishingCompassSolver.WishingCompassSolverState.PROCESSING_FIRST_USE) {
                    WishingCompassSolver.state = WishingCompassSolver.WishingCompassSolverState.NEED_SECOND_COMPASS
                    return
                }
            }
        }
    }
}