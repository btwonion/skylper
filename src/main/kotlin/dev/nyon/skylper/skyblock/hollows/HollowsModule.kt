package dev.nyon.skylper.skyblock.hollows

import de.hysky.skyblocker.utils.waypoint.Waypoint
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.IslandChangeEvent
import dev.nyon.skylper.extensions.math.blockPos
import dev.nyon.skylper.skyblock.PlayerSessionData
import dev.nyon.skylper.skyblock.data.currentProfile
import dev.nyon.skylper.skyblock.data.playerData
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import dev.nyon.skylper.skyblock.hollows.solvers.wishing.WishingCompassSolver
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.world.phys.AABB

@Suppress("SpellCheckingInspection")
object HollowsModule {
    private val nucleusNames = listOf("Crystal Nucleus")
    private val jungleNames = listOf("Jungle", "Jungle Temple")
    private val goblinHideoutNames = listOf("Goblin Holdout", "Goblin Queen's Den")
    private val mithrilDepositsNames = listOf("Mithril Deposits", "Mines of Divan")
    private val precursorRemnantsNames = listOf("Precursor Remnants", "Lost Precursor City")
    private val magmaFieldsNames = listOf("Magma Fields", "Khazad-dûm")
    private val extraNames = listOf("Dragon's Lair", "Fairy Grotto")
    private val crystalHollowsNames = listOf(
        nucleusNames,
        jungleNames,
        goblinHideoutNames,
        mithrilDepositsNames,
        precursorRemnantsNames,
        magmaFieldsNames,
        extraNames
    ).flatten()
    val hollowsBox = AABB(201.0, 30.0, 201.0, 824.0, 189.0, 824.0)

    val foundCrystals: Set<Crystal>
        get() {
            return playerData.currentProfile?.crystalHollows?.foundCrystals ?: mutableSetOf()
        }

    val isPlayerInHollows: Boolean
        get() {
            return crystalHollowsNames.contains(PlayerSessionData.currentIsland)
        }

    private val defaultCrystalHollowsWaypoints: Map<String, Waypoint> = mapOf(
        HollowsStructure.CRYSTAL_NUCLEUS.internalWaypointName to HollowsStructureWaypoint(
            HollowsStructure.CRYSTAL_NUCLEUS.box.center,
            HollowsStructure.CRYSTAL_NUCLEUS
        )
    )
    val waypoints: MutableMap<String, Waypoint> = mutableMapOf()

    fun init() {
        WishingCompassSolver.init()

        listenEvent<IslandChangeEvent> {
            if (it.next?.contains("Crystal Hollows") == false) waypoints.clear()
        }
        WorldRenderEvents.AFTER_TRANSLUCENT.register { context ->
            if (!isPlayerInHollows) return@register
            waypoints.also { it.putAll(defaultCrystalHollowsWaypoints) }.values.forEach { waypoint ->
                waypoint.render(context)
            }
        }
    }
}