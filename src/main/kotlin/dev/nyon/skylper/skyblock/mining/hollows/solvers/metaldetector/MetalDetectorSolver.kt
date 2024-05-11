package dev.nyon.skylper.skyblock.mining.hollows.solvers.metaldetector

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import dev.nyon.skylper.skyblock.mining.hollows.locations.HollowsLocation
import dev.nyon.skylper.skyblock.mining.hollows.locations.PreDefinedHollowsLocationSpecific
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
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
    private var successWaypoint: Waypoint? = null
    private var lastChestFound: Instant? = null

    @Suppress("unused")
    private val chatEvent = listenEvent<MessageEvent, Unit> {
        val playerPos = minecraft.player?.position() ?: return@listenEvent
        if (!config.mining.crystalHollows.metalDetectorHelper) return@listenEvent
        if (!divanCheck()) return@listenEvent
        if (minesCenter == null) return@listenEvent

        val stringMessage = it.text.string
        val now = Clock.System.now() // Handle found treasure
        if (stringMessage.contains(FOUND_TREASURE_MESSAGE_START) && stringMessage.contains(FOUND_TREASURE_MESSAGE_END)) {
            successWaypoint = null
            lastChestFound = now
        }

        if (successWaypoint != null) return@listenEvent

        // Calculate distance to treasure from action bar
        if (!stringMessage.contains(TREASURE_DISTANCE_MESSAGE)) return@listenEvent
        val distanceString = stringMessage.replace(TREASURE_DISTANCE_MESSAGE, "").dropLast(1)
        val distance = distanceString.toDoubleOrNull() ?: return@listenEvent

        if (lastChestFound == null || now - lastChestFound!! > 500.milliseconds) solve(distance, playerPos)
    }

    @Suppress("unused")
    private val tickEvent = listenEvent<TickEvent, Unit> {
        if (!divanCheck()) return@listenEvent
        if (successWaypoint != null && minecraft.player?.position()
                ?.distanceTo(successWaypoint!!.pos)!! < 1.5
        ) successWaypoint = null
        if (minesCenter != null) return@listenEvent

        // Find keeper
        val armorStands = minecraft.level?.findArmorStandsWithName(KEEPER_OF_NAME) ?: return@listenEvent
        if (armorStands.isEmpty()) return@listenEvent
        val keeperStand = armorStands.first()
        val keeperIdentifier = keeperStand.customName!!.string.replace(KEEPER_OF_NAME, "")
        val keeper = DivanMinesKeeper.entries.find { it.identifier == keeperIdentifier } ?: return@listenEvent

        // set mines center
        val crystalCoords = keeperStand.position().add(keeper.offset)
        minesCenter = crystalCoords
        EventHandler.invokeEvent(
            LocatedHollowsStructureEvent(
                HollowsLocation(crystalCoords.add(0.0, 2.0, 0.0), PreDefinedHollowsLocationSpecific.MINES_OF_DIVAN),
                true
            )
        )

        // Init possible chest locations
        actualChestPositions = detectorChestOffsets.getActualPositions(minesCenter ?: return@listenEvent)
    }

    @Suppress("unused")
    private val levelChangeEvent = listenEvent<LevelChangeEvent, Unit> { reset() }

    @Suppress("unused")
    private val renderEvent = listenEvent<RenderAfterTranslucentEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        successWaypoint?.render(it.context)
    }

    private fun reset() {
        minesCenter = null
        actualChestPositions = null
        successWaypoint = null
    }

    private fun solve(
        distance: Double, playerPos: Vec3
    ) {
        val distances = actualChestPositions?.associateWith {
            (it.distanceTo(playerPos) - distance)
        }?.filter { it.value >= -0.4 && it.value <= 0.4 } ?: return
        val sorted = distances.toList().sortedBy { it.second }
        val pos = sorted.firstOrNull()?.first ?: return
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
            WaypointType.FILLED_WITH_BEAM,
            ChatFormatting.RED.color!!
        )
    }

    private fun divanCheck(): Boolean {
        return HollowsModule.isPlayerInHollows && PlayerSessionData.currentZone == "Mines of Divan"
    }
}
