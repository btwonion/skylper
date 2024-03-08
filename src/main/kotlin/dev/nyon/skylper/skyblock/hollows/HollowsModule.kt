package dev.nyon.skylper.skyblock.hollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.AreaChangeEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.hollows.locations.ChatStructureListener
import dev.nyon.skylper.skyblock.hollows.locations.CrystalRunListener
import dev.nyon.skylper.skyblock.hollows.locations.NameTagEntityListener
import dev.nyon.skylper.skyblock.hollows.locations.PlayerChatLocationListener
import dev.nyon.skylper.skyblock.hollows.render.ChestHighlighter
import dev.nyon.skylper.skyblock.hollows.solvers.metaldetector.MetalDetectorSolver
import dev.nyon.skylper.skyblock.hollows.tracker.PassExpiryTracker
import dev.nyon.skylper.skyblock.hollows.tracker.PowderGrindingTracker
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB

object HollowsModule {
    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val isPlayerInHollows: Boolean
        get() {
            val areaMatch = PlayerSessionData.currentArea?.contains("Crystal Hollows") == true
            val posMatch = hollowsBox.contains(
                minecraft.player?.position() ?: return false
            )

            return areaMatch && posMatch
        }

    private val nucleusWaypoint = HollowsStructure.CRYSTAL_NUCLEUS.internalWaypointName to Waypoint(
        Component.literal(HollowsStructure.CRYSTAL_NUCLEUS.displayName),
        HollowsStructure.CRYSTAL_NUCLEUS.box.center,
        WaypointType.BEAM,
        HollowsStructure.CRYSTAL_NUCLEUS.waypointColor
    )
    val waypoints: MutableMap<String, Waypoint> = mutableMapOf()

    fun init() {
        PlayerChatLocationListener.init()
        NameTagEntityListener.init()
        ChatStructureListener.init()
        ChestHighlighter.init()
        PowderGrindingTracker.init()
        CrystalRunListener.init()
        PassExpiryTracker.init()
        MetalDetectorSolver.init()

        handleWaypoints()
    }

    private fun handleWaypoints() {
        listenEvent<LevelChangeEvent> {
            waypoints.clear()
            if (config.mining.crystalHollows.hollowsWaypoints.nucleus) waypoints[nucleusWaypoint.first] =
                nucleusWaypoint.second
        }
        listenEvent<AreaChangeEvent> {
            if (it.next?.contains("Crystal Hollows") == false) waypoints.clear()
            else waypoints[nucleusWaypoint.first] = nucleusWaypoint.second
        }
        listenEvent<RenderAfterTranslucentEvent> {
            if (!isPlayerInHollows) return@listenEvent
            waypoints.values.forEach { waypoint ->
                waypoint.render(it.context)
            }
        }
    }
}