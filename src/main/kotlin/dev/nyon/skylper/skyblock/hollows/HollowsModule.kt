package dev.nyon.skylper.skyblock.hollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.AreaChangeEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
import dev.nyon.skylper.skyblock.hollows.locations.ChatStructureListener
import dev.nyon.skylper.skyblock.hollows.locations.PlayerChatLocationListener
import dev.nyon.skylper.skyblock.hollows.render.ChestHighlighter
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import net.minecraft.world.phys.AABB

object HollowsModule {
    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val foundCrystals: Set<Crystal>
        get() {
            return playerData.currentProfile?.crystalHollows?.foundCrystals ?: mutableSetOf()
        }

    val isPlayerInHollows: Boolean
        get() {
            val areaMatch = PlayerSessionData.currentArea?.contains("Crystal Hollows") == true
            val posMatch = hollowsBox.contains(
                minecraft.player?.position() ?: return false
            )

            return areaMatch && posMatch
        }

    private val nucleusWaypoint = HollowsStructure.CRYSTAL_NUCLEUS.internalWaypointName to HollowsStructureWaypoint(
        HollowsStructure.CRYSTAL_NUCLEUS.box.center, HollowsStructure.CRYSTAL_NUCLEUS, false
    )
    val waypoints: MutableMap<String, HollowsStructureWaypoint> = mutableMapOf()

    fun init() {
        PlayerChatLocationListener.init()
        ChatStructureListener.init()
        ChestHighlighter.init()

        listenEvent<LevelChangeEvent> {
            waypoints.clear()
            waypoints[nucleusWaypoint.first] = nucleusWaypoint.second
        }
        listenEvent<AreaChangeEvent> {
            if (it.next?.contains("Crystal Hollows") == false) waypoints.clear()
            else waypoints[nucleusWaypoint.first] = nucleusWaypoint.second
        }
        listenEvent<RenderAfterTranslucentEvent> {
            if (!config.crystalHollows.showWaypoints) return@listenEvent
            if (!isPlayerInHollows) return@listenEvent
            waypoints.values.forEach { waypoint ->
                waypoint.render(it.context)
            }
        }
    }
}