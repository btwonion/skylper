package dev.nyon.skylper.skyblock.hollows.solvers.wishing

import dev.nyon.skylper.skyblock.hollows.HollowsZone
import dev.nyon.skylper.skyblock.hollows.solvers.wishing.WishingCompassSolver.WishingCompassSolverState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.core.BlockPos

object CompassHandler {
    private const val MINIMUM_DISTANCE_SQ_BETWEEN_COMPASSES = 64.0

    fun handleCompassUse(playerPos: BlockPos): HandleCompassResult? {
        var lastCompassUsed: Instant? = null

        when (WishingCompassSolver.state) {
            WishingCompassSolverState.PROCESSING_FIRST_USE -> {
                if (WishingCompassSolver.firstCompass != null) lastCompassUsed = WishingCompassSolver.firstCompass?.used
                if (lastCompassUsed != null && (Clock.System.now() > lastCompassUsed + ParticleHandler.MAX_PARTICLE_APPEAR_TIME)) return HandleCompassResult.NO_PARTICLES_FOR_PREVIOUS_COMPASS
                return HandleCompassResult.STILL_PROCESSING_PRIOR_USE
            }

            WishingCompassSolverState.SOLVED, WishingCompassSolverState.FAILED_EXCEPTION, WishingCompassSolverState.FAILED_TIMEOUT_NO_REPEATING, WishingCompassSolverState.FAILED_TIMEOUT_NO_PARTICLES, WishingCompassSolverState.FAILED_INVALID_SOLUTION, WishingCompassSolverState.FAILED_INTERSECTION_CALCULATION -> WishingCompassSolver.reset()
            WishingCompassSolverState.NOT_STARTED -> {
                if (HollowsZone.CRYSTAL_NUCLEUS.box.contains(playerPos.center)) return HandleCompassResult.PLAYER_IN_NUCLEUS

                WishingCompassSolver.firstCompass = Compass(pos = playerPos, used = Clock.System.now())
                WishingCompassSolver.seenParticles.clear()
                WishingCompassSolver.state = WishingCompassSolverState.PROCESSING_FIRST_USE
                WishingCompassSolver.possibleTargets = WishingCompassSolver.calculatePossibleTargets()
                return HandleCompassResult.SUCCESS
            }

            WishingCompassSolverState.NEED_SECOND_COMPASS -> {
                if (MINIMUM_DISTANCE_SQ_BETWEEN_COMPASSES > (WishingCompassSolver.firstCompass?.pos?.distSqr(playerPos)
                        ?: return HandleCompassResult.POSSIBLE_TARGETS_CHANGED)) return HandleCompassResult.LOCATION_TOO_CLOSE

                var firstZone: HollowsZone? = null
                var playerZone: HollowsZone? = null
                HollowsZone.entries.forEach {
                    if (it.box.contains(WishingCompassSolver.firstCompass?.pos?.center ?: return@forEach)) firstZone =
                        it
                    if (it.box.contains(playerPos.center)) playerZone = it
                }

                if (WishingCompassSolver.possibleTargets != WishingCompassSolver.calculatePossibleTargets() || firstZone != playerZone) {
                    WishingCompassSolver.reset()
                    return HandleCompassResult.POSSIBLE_TARGETS_CHANGED
                }

                WishingCompassSolver.secondCompass = Compass(pos = playerPos, used = Clock.System.now())
                WishingCompassSolver.state = WishingCompassSolverState.PROCESSING_SECOND_USE
                return HandleCompassResult.SUCCESS
            }

            else -> {}
        }

        return null
    }


    enum class HandleCompassResult {
        SUCCESS,
        LOCATION_TOO_CLOSE,
        STILL_PROCESSING_PRIOR_USE,
        POSSIBLE_TARGETS_CHANGED,
        NO_PARTICLES_FOR_PREVIOUS_COMPASS,
        PLAYER_IN_NUCLEUS
    }
}