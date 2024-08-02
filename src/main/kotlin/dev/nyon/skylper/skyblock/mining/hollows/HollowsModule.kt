package dev.nyon.skylper.skyblock.mining.hollows

import dev.nyon.skylper.extensions.event.AreaChangeEvent
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.LevelChangeEvent
import dev.nyon.skylper.extensions.event.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.event.RenderAfterTranslucentEvent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.hollows.locations.*
import dev.nyon.skylper.skyblock.mining.hollows.render.ChestHighlighter
import dev.nyon.skylper.skyblock.mining.hollows.render.ChestParticleHighlighter
import dev.nyon.skylper.skyblock.mining.hollows.tracker.PassExpiryTracker
import dev.nyon.skylper.skyblock.mining.hollows.tracker.nucleus.CrystalRunListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object HollowsModule {
    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val isPlayerInHollows: Boolean
        get() {
            val areaMatch = PlayerSessionData.currentArea?.contains("Crystal Hollows") == true
            val posMatch = hollowsBox.contains(minecraft.player?.position() ?: return false)

            return areaMatch && posMatch
        }

    private val nucleusWaypoint = HollowsLocation(
        Vec3(513.5, 115.0, 513.5), CreationReason.STATIC, PreDefinedHollowsLocationSpecific.CRYSTAL_NUCLEUS
    )
    private val mutex = Mutex()
    val waypoints: MutableSet<HollowsLocation> = mutableSetOf()

    fun init() {
        PlayerChatLocationListener
        NameTagEntityListener
        SideboardLocationListener
        ChestHighlighter.init()
        CrystalRunListener.init()
        PassExpiryTracker.init()
        ChestParticleHighlighter
        registerHollowsLocationHotkey()

        handleWaypoints()
    }

    private fun handleWaypoints() {
        listenEvent<LevelChangeEvent, Unit> {
            resetWaypoints()
        }
        listenEvent<AreaChangeEvent, Unit> {
            resetWaypoints()
        }
        listenEvent<RenderAfterTranslucentEvent, Unit> {
            if (!isPlayerInHollows) return@listenEvent
            mcScope.launch {
                mutex.withLock {
                    waypoints.forEach { location ->
                        if (!location.isEnabled) return@forEach
                        location.waypoint.render(context)
                    }
                }
            }
        }
        listenEvent<LocatedHollowsStructureEvent, Unit> {
            if (!isPlayerInHollows) return@listenEvent
            val currentPos = minecraft.player?.position() ?: return@listenEvent
            independentScope.launch {
                mutex.withLock { // Explicitly handle Fairy Grotto
                    if (location.specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO && waypoints.filter { it.specific == PreDefinedHollowsLocationSpecific.FAIRY_GROTTO }
                            .any { it.pos.distanceTo(currentPos) < 100 }) {
                        return@withLock
                    }
                    val existing = waypoints.find { it.specific == location.specific }
                    if (existing != null) {
                        if (existing.reason.priority < location.reason.priority) return@withLock
                        waypoints.remove(existing)
                    }

                    waypoints.add(location)
                }
            }
        }
    }

    private fun resetWaypoints() {
        independentScope.launch {
            mutex.withLock {
                waypoints.clear()
                waypoints.add(nucleusWaypoint)
            }
        }
    }
}
