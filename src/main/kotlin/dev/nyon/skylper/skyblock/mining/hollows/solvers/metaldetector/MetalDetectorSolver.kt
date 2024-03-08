package dev.nyon.skylper.skyblock.mining.hollows.solvers.metaldetector

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.phys.Vec3
import kotlin.time.Duration.Companion.milliseconds

object MetalDetectorSolver {
    private const val TREASURE_DISTANCE_MESSAGE = "§3§lTREASURE: §b"
    private const val FOUND_TREASURE_MESSAGE_START = "You found "
    private const val FOUND_TREASURE_MESSAGE_END = "with your Metal Detector"
    private const val KEEPER_OF_NAME = "Keeper of "
    private const val TRANSLATION_NAMESPACE = "chat.skylper.hollows.solver.metaldetector"

    private var minesCenter: Vec3? = null
    private var actualChestPositions: List<Vec3>? = null
    private var state: MetalDetectorState = MetalDetectorState.Idle
    private var successWaypoint: Waypoint? = null
    private var lastChestFound: Instant? = null

    fun init() {
        listenChat()
        listenWorldChange()
        calculateCenter()
        renderWaypoint()
    }

    private fun listenChat() = listenEvent<MessageEvent> {
        val playerPos = minecraft.player?.position()
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        if (!config.mining.crystalHollows.metalDetectorHelper) return@listenEvent
        if (minesCenter == null) return@listenEvent
        val stringMessage = it.text.string
        val now = Clock.System.now()
        if (stringMessage.contains(FOUND_TREASURE_MESSAGE_START) && stringMessage.contains(FOUND_TREASURE_MESSAGE_END)) {
            successWaypoint = null
            lastChestFound = now
        }
        if (successWaypoint != null) return@listenEvent
        if (!stringMessage.contains(TREASURE_DISTANCE_MESSAGE)) return@listenEvent
        playerPos ?: return@listenEvent
        val distanceString = stringMessage.replace(TREASURE_DISTANCE_MESSAGE, "").dropLast(1)
        val distance = distanceString.toDoubleOrNull() ?: return@listenEvent
        if (state == MetalDetectorState.Solving) return@listenEvent
        if (lastChestFound == null || now - lastChestFound!! > 500.milliseconds) solve(distance, playerPos)
    }

    private fun calculateCenter() = listenEvent<TickEvent> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        if (PlayerSessionData.currentZone != "Mines of Divan") return@listenEvent
        if (successWaypoint != null && minecraft.player?.position()
                ?.distanceTo(successWaypoint!!.pos)!! < 1.0
        ) successWaypoint = null
        if (minesCenter != null) return@listenEvent

        val armorStands = minecraft.level?.getEntitiesOfClass(
            ArmorStand::class.java, minecraft.player?.radiusBox(50) ?: return@listenEvent
        )?.filter { it.customName?.string?.contains(KEEPER_OF_NAME) == true } ?: return@listenEvent
        if (armorStands.isEmpty()) return@listenEvent
        val keeperStand = armorStands.first()
        val keeperIdentifier = keeperStand.customName!!.string.replace(KEEPER_OF_NAME, "")
        val keeper = DivanMinesKeeper.entries.find { it.identifier == keeperIdentifier } ?: return@listenEvent
        minesCenter = keeperStand.position().add(keeper.offset)
        actualChestPositions = detectorChestOffsets.getActualPositions(minesCenter ?: return@listenEvent)
    }

    private fun listenWorldChange() = listenEvent<LevelChangeEvent> { reset() }

    private fun renderWaypoint() = listenEvent<RenderAfterTranslucentEvent> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        successWaypoint?.render(it.context)
    }

    private fun reset() {
        minesCenter = null
        actualChestPositions = null
        state = MetalDetectorState.Idle
        successWaypoint = null
    }

    private fun solve(distance: Double, playerPos: Vec3) {
        state = MetalDetectorState.Solving
        val distances = actualChestPositions?.associateWith {
            it.distanceTo(playerPos) - distance
        }?.filter { it.value >= -0.3 } ?: return
        val sorted = distances.toList().sortedBy { it.second }
        val pos = sorted.firstOrNull()?.first ?: playerPos.add(Vec3(0.0, -1.0, 0.0))
        state = MetalDetectorState.Idle
        success(pos)
        return
    }

    private fun success(pos: Vec3) {
        minecraft.player?.sendSystemMessage(
            Component.translatable("$TRANSLATION_NAMESPACE.success").withColor(ChatFormatting.DARK_GREEN.color!!)
        )
        successWaypoint = Waypoint(
            Component.literal("Treasure").withColor(ChatFormatting.RED.color!!),
            pos,
            WaypointType.OUTLINE_WITH_BEAM,
            ChatFormatting.RED.color!!
        )
    }
}