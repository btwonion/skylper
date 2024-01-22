package dev.nyon.skylper.skyblock.hollows.solvers.wishing

import dev.nyon.skylper.extensions.internalName
import dev.nyon.skylper.extensions.math.Vec3Comparable
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.PlayerSessionData
import dev.nyon.skylper.skyblock.hollows.*
import dev.nyon.skylper.skyblock.hollows.HollowsModule.foundCrystals
import dev.nyon.skylper.skyblock.hollows.solvers.wishing.CompassHandler.handleCompassUse
import kotlinx.datetime.Instant
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

object WishingCompassSolver {
    private val JUNGLE_DOOR_OFFSET_FROM_CRYSTAL = Vec3Comparable(-57.0, 36.0, -21.0)
    private const val WISHING_COMPASS_NAME = "WISHING_COMPASS"

    var state: WishingCompassSolverState = WishingCompassSolverState.NOT_STARTED
    var firstCompass: Compass? = null
    var secondCompass: Compass? = null
    val seenParticles: ArrayDeque<Pair<Vec3Comparable, Instant>> = ArrayDeque(0)
    var possibleTargets: Set<HollowsStructure> = setOf()

    private val kingsScentPresent: Boolean
        get() {
            return PlayerSessionData.footer.string.contains("King's Scent I")
        }
    private val jungleKeyInInventory: Boolean
        get() {
            return minecraft.player?.inventory?.items?.any {
                !it.isEmpty && it.internalName == "JUNGLE_KEY"
            } ?: return false
        }

    fun init() {
        ParticleHandler.initParticleListener()
        registerCompassListener()
    }

    private fun registerCompassListener() {
        UseItemCallback.EVENT.register { player: Player, _: Level, hand: InteractionHand ->
            val usedItem = player.getItemInHand(hand)

            val result = handleItemUse(usedItem, player)

            return@register InteractionResultHolder(result, usedItem)
        }

        UseBlockCallback.EVENT.register { player: Player, _, hand: InteractionHand, _ ->
            val usedItem = player.getItemInHand(hand)

            val result = handleItemUse(usedItem, player)

            return@register result
        }
    }

    fun processBothCompasses(): Pair<Vec3Comparable, Set<HollowsStructure>>? {
        val firstCompass = WishingCompassSolver.firstCompass ?: return null
        val secondCompass = WishingCompassSolver.secondCompass ?: return null

        val solutionIntersectionLine = firstCompass.line?.getIntersectionLineSegment(secondCompass.line ?: return null)
        if (solutionIntersectionLine == null) {
            state = WishingCompassSolverState.FAILED_INTERSECTION_CALCULATION
            return null
        }

        var solution = Vec3Comparable(solutionIntersectionLine.getMidpoint())

        val firstDirection = firstCompass.direction ?: return null
        val firstSolutionDirection = firstCompass.directionTo(solution) ?: return null
        val secondDirection = secondCompass.direction ?: return null
        val secondSolutionDirection = secondCompass.directionTo(solution) ?: return null

        if (!firstDirection.signEquals(firstSolutionDirection) || !secondDirection.signEquals(secondSolutionDirection) || !HollowsModule.hollowsBox.contains(
                solution
            )) {
            state = WishingCompassSolverState.FAILED_INVALID_SOLUTION
            return null
        }

        val possibleTargets = solveFinalSolutionTargets(
            solution.hollowsZone ?: return null, foundCrystals, WishingCompassSolver.possibleTargets, solution
        )

        if (possibleTargets.size == 1 && possibleTargets.contains(HollowsStructure.JUNGLE_TEMPLE)) solution =
            solution.add(JUNGLE_DOOR_OFFSET_FROM_CRYSTAL)

        state = WishingCompassSolverState.SOLVED
        return solution to possibleTargets
    }

    private fun handleItemUse(item: ItemStack, player: Player): InteractionResult {
        if (player != minecraft.player) return InteractionResult.PASS
        if (item.isEmpty || !item.`is`(Items.PLAYER_HEAD)) return InteractionResult.PASS
        if (item.internalName != WISHING_COMPASS_NAME) return InteractionResult.PASS
        if (!HollowsModule.isPlayerInHollows) return InteractionResult.PASS

        val pos = player.onPos
        val useResult = handleCompassUse(pos)

        return ResultHandler.handleCompassResult(useResult)
    }

    fun reset() {
        state = WishingCompassSolverState.NOT_STARTED
        firstCompass = null
        secondCompass = null
        possibleTargets = setOf()
    }

    fun calculatePossibleTargets(): MutableSet<HollowsStructure> {
        val candidateTargets = mutableSetOf(HollowsStructure.CRYSTAL_NUCLEUS)

        Crystal.entries.toMutableSet().also { it.removeAll(foundCrystals) }.forEach { crystal ->
            when (crystal) {
                Crystal.JADE -> candidateTargets.add(HollowsStructure.MINES_OF_DIVAN)
                Crystal.AMBER -> candidateTargets.add(if (kingsScentPresent) HollowsStructure.GOBLIN_QUEEN else HollowsStructure.GOBLIN_KING)
                Crystal.TOPAZ -> candidateTargets.add(HollowsStructure.KHAZAD_DUM)
                Crystal.AMETHYST -> candidateTargets.add(if (jungleKeyInInventory) HollowsStructure.JUNGLE_TEMPLE else HollowsStructure.ODAWA)
                Crystal.SAPPHIRE -> candidateTargets.add(HollowsStructure.PRECURSOR_CITY)
            }
        }

        return candidateTargets
    }

    private fun solveFinalSolutionTargets(
        usedZone: HollowsZone,
        foundCrystals: Set<Crystal>,
        possibleTargets: Set<HollowsStructure>,
        correctSolution: Vec3Comparable
    ): Set<HollowsStructure> {
        val solutionPossibleTargets = possibleTargets.toMutableSet()
        val solutionZone = correctSolution.hollowsZone

        if (solutionZone == HollowsZone.CRYSTAL_NUCLEUS) return solutionPossibleTargets
        solutionPossibleTargets.remove(HollowsStructure.CRYSTAL_NUCLEUS)

        solutionPossibleTargets.removeIf { structure ->
            if (structure == HollowsStructure.KHAZAD_DUM && usedZone == HollowsZone.JUNGLE && !foundCrystals.contains(
                    Crystal.AMETHYST
                )) return@removeIf true
            val yMismatch = correctSolution.y < structure.minY || correctSolution.y > structure.maxY
            val xzMismatch =
                correctSolution.x > structure.zone.box.maxX + structure.box.maxX || correctSolution.z < structure.zone.box.minZ - structure.box.maxZ
            yMismatch && xzMismatch
        }

        return solutionPossibleTargets
    }

    enum class WishingCompassSolverState {
        NOT_STARTED,
        PROCESSING_FIRST_USE,
        NEED_SECOND_COMPASS,
        PROCESSING_SECOND_USE,
        SOLVED,
        FAILED_EXCEPTION,
        FAILED_TIMEOUT_NO_REPEATING,
        FAILED_TIMEOUT_NO_PARTICLES,
        FAILED_INTERSECTION_CALCULATION,
        FAILED_INVALID_SOLUTION,
    }
}