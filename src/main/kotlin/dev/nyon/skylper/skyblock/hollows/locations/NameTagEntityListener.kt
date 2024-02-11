package dev.nyon.skylper.skyblock.hollows.locations

import dev.nyon.skylper.extensions.EntitySpawnEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.color
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint

object NameTagEntityListener {

    fun init() {
        listenEvent<EntitySpawnEvent> { (entity) ->
            val name = entity.displayName?.string ?: return@listenEvent
            val structure = when {
                name.contains("Boss Corleone") -> HollowsStructure.CORLEONE
                name.contains("Key Guardian") -> HollowsStructure.KEY_GUARDIAN
                else -> null
            } ?: return@listenEvent

            if (structure.isWaypointEnabled()) HollowsModule.waypoints[structure.internalWaypointName] = HollowsStructureWaypoint(
                entity.position(), structure.displayName, entity.y.toInt(), structure.waypointColor.color
            )
        }
    }
}