package dev.nyon.skylper.skyblock.hollows.locations

import dev.nyon.skylper.extensions.EntitySpawnEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.color
import dev.nyon.skylper.extensions.hasMaxHealth
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import dev.nyon.skylper.skyblock.hollows.HollowsStructure
import dev.nyon.skylper.skyblock.hollows.render.HollowsStructureWaypoint
import net.minecraft.world.entity.LivingEntity

object NameTagEntityListener {

    fun init() {
        listenEvent<EntitySpawnEvent> { (entity) ->
            if (!HollowsModule.isPlayerInHollows) return@listenEvent
            val livingEntity = entity as? LivingEntity ?: return@listenEvent
            val name = entity.displayName?.string ?: return@listenEvent
            val structure = when {
                name.contains("Team Treasurite") && livingEntity.hasMaxHealth(1000000f) -> HollowsStructure.CORLEONE
                name.contains("Key Guardian") -> HollowsStructure.KEY_GUARDIAN
                else -> null
            } ?: return@listenEvent

            if (structure.isWaypointEnabled()) HollowsModule.waypoints[structure.internalWaypointName] =
                HollowsStructureWaypoint(
                    entity.position(), structure.displayName, entity.y.toInt(), structure.waypointColor.color
                )
        }
    }
}