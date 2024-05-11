package dev.nyon.skylper.skyblock.mining.hollows

import dev.nyon.skylper.extensions.AreaChangeEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.hollows.locations.*
import dev.nyon.skylper.skyblock.mining.hollows.render.ChestHighlighter
import dev.nyon.skylper.skyblock.mining.hollows.render.ChestParticleHighlighter
import dev.nyon.skylper.skyblock.mining.hollows.solvers.metaldetector.MetalDetectorSolver
import dev.nyon.skylper.skyblock.mining.hollows.tracker.PassExpiryTracker
import net.minecraft.world.phys.AABB

object HollowsModule {
    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val isPlayerInHollows: Boolean
        get() {
            val areaMatch = PlayerSessionData.currentArea?.contains("Crystal Hollows") == true
            val posMatch = hollowsBox.contains(minecraft.player?.position() ?: return false)

            return areaMatch && posMatch
        }

    private val nucleusWaypoint = HollowsLocation(hollowsBox.center, PreDefinedHollowsLocationSpecific.CRYSTAL_NUCLEUS)
    val waypoints: MutableSet<HollowsLocation> = mutableSetOf()

    fun init() {
        PlayerChatLocationListener
        NameTagEntityListener
        SideboardLocationListener
        ChestHighlighter.init()
        CrystalRunListener.init()
        PassExpiryTracker.init()
        MetalDetectorSolver
        ChestParticleHighlighter
        registerHollowsLocationHotkey()

        handleWaypoints()
    }

    private fun handleWaypoints() {
        listenEvent<LevelChangeEvent, Unit> {
            waypoints.clear()
            waypoints.add(nucleusWaypoint)
        }
        listenEvent<AreaChangeEvent, Unit> {
            if (it.next?.contains("Crystal Hollows") == false) {
                waypoints.clear()
            } else {
                waypoints.add(nucleusWaypoint)
            }
        }
        listenEvent<RenderAfterTranslucentEvent, Unit> {
            if (!isPlayerInHollows) return@listenEvent
            waypoints.forEach { location ->
                if (!location.isEnabled) return@forEach
                location.waypoint.render(it.context)
            }
        }
        listenEvent<LocatedHollowsStructureEvent, Unit> { (location) ->
            if (!isPlayerInHollows) return@listenEvent
            val isFairyGrotto = location.specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO
            val currentPos = minecraft.player?.position() ?: return@listenEvent
            if (isFairyGrotto && waypoints.any {
                    it.specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO && it.pos.closerThan(
                        currentPos, 25.0, 100.0
                    )
                }) {
                return@listenEvent
            }
            if (isFairyGrotto || waypoints.none { it.specific == location.specific }) {
                waypoints.add(location)
            }
        }
    }
}
