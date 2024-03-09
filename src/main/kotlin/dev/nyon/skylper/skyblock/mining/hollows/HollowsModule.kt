package dev.nyon.skylper.skyblock.mining.hollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.AreaChangeEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.hollows.locations.ChatStructureListener
import dev.nyon.skylper.skyblock.mining.hollows.locations.CrystalRunListener
import dev.nyon.skylper.skyblock.mining.hollows.locations.NameTagEntityListener
import dev.nyon.skylper.skyblock.mining.hollows.locations.PlayerChatLocationListener
import dev.nyon.skylper.skyblock.mining.hollows.render.ChestHighlighter
import dev.nyon.skylper.skyblock.mining.hollows.render.ChestParticleHighlighter
import dev.nyon.skylper.skyblock.mining.hollows.solvers.metaldetector.MetalDetectorSolver
import dev.nyon.skylper.skyblock.mining.hollows.tracker.PassExpiryTracker
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
        CrystalRunListener.init()
        PassExpiryTracker.init()
        MetalDetectorSolver.init()
        ChestParticleHighlighter

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