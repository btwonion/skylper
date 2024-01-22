package dev.nyon.skylper.skyblock.hollows.solvers.wishing

import dev.nyon.skylper.extensions.math.Vec3Comparable
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult

object ResultHandler {
    private const val NO_TARGET_MESSAGE = "The Wishing Compass can't seem to locate anything!"

    fun handleSolveResult(solutionPos: Vec3Comparable, possibleTargets: Set<HollowsStructure>) {
        when (WishingCompassSolver.state) {
            WishingCompassSolver.WishingCompassSolverState.SOLVED -> handleSolution(solutionPos, possibleTargets)
            WishingCompassSolver.WishingCompassSolverState.FAILED_EXCEPTION -> sendRedMessage("failed_exception")
            WishingCompassSolver.WishingCompassSolverState.FAILED_TIMEOUT_NO_REPEATING -> sendRedMessage("failed_no_repeating")
            WishingCompassSolver.WishingCompassSolverState.FAILED_TIMEOUT_NO_PARTICLES -> if (!minecraft.gui.chat.recentChat
                    .takeLast(10)
                    .contains(NO_TARGET_MESSAGE)) sendRedMessage("failed_no_particles")
            WishingCompassSolver.WishingCompassSolverState.FAILED_INTERSECTION_CALCULATION -> sendRedMessage("failed_intersection_calculation")
            WishingCompassSolver.WishingCompassSolverState.FAILED_INVALID_SOLUTION -> sendRedMessage("failed_invalid_solution")
            WishingCompassSolver.WishingCompassSolverState.NEED_SECOND_COMPASS -> sendRedMessage("failed_need_second_compass")
            else -> {}
        }
    }

    fun handleCompassResult(result: CompassHandler.HandleCompassResult?): InteractionResult {
        return when (result) {
            CompassHandler.HandleCompassResult.SUCCESS -> InteractionResult.PASS
            CompassHandler.HandleCompassResult.STILL_PROCESSING_PRIOR_USE -> sendRedMessage("still_processing")
            CompassHandler.HandleCompassResult.LOCATION_TOO_CLOSE -> sendRedMessage("too_close")
            CompassHandler.HandleCompassResult.POSSIBLE_TARGETS_CHANGED -> sendRedMessage("possible_targets_changed")
            CompassHandler.HandleCompassResult.PLAYER_IN_NUCLEUS -> sendRedMessage("nucleus")
            CompassHandler.HandleCompassResult.NO_PARTICLES_FOR_PREVIOUS_COMPASS -> sendRedMessage(
                "no_particles", InteractionResult.PASS
            )

            null -> {
                WishingCompassSolver.reset()
                sendRedMessage("unknown_error")
                return InteractionResult.FAIL
            }
        }
    }

    private fun handleSolution(pos: Vec3Comparable, structure: Set<HollowsStructure>) {
        if (HollowsStructure.CRYSTAL_NUCLEUS.box.contains(pos)) {
            sendSuccessMessage("solved_nucleus")
            return
        }



        minecraft.player?.sendSystemMessage(
            Component.translatable(
                "chat.skylper.hollows.compass_solver.solved", structure.first().displayName, pos.x, pos.y, pos.z
            )
        )
    }

    private fun sendSuccessMessage(key: String, vararg args: Any) {
        minecraft.player?.sendSystemMessage(
            Component.translatable("chat.skylper.hollows.compass_solver.$key", args).withColor(ChatFormatting.DARK_GREEN.color!!)
        )
    }

    private fun sendRedMessage(key: String, result: InteractionResult = InteractionResult.FAIL): InteractionResult {
        minecraft.player?.sendSystemMessage(
            Component.translatable("chat.skylper.hollows.compass_solver.$key").withColor(ChatFormatting.RED.color!!)
        )
        return result
    }
}