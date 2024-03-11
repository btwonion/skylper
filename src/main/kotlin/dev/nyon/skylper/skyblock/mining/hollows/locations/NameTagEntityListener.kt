package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.EntitySpawnEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.hasMaxHealth
import dev.nyon.skylper.extensions.render.waypoint.Waypoint
import dev.nyon.skylper.extensions.render.waypoint.WaypointType
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import dev.nyon.skylper.skyblock.mining.hollows.HollowsStructure
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity

object NameTagEntityListener {

    fun init() {
        listenEvent<EntitySpawnEvent, Unit> { (entity) ->
            if (!HollowsModule.isPlayerInHollows) return@listenEvent
            val livingEntity = entity as? LivingEntity ?: return@listenEvent
            val name = entity.displayName?.string ?: return@listenEvent
            val structure = when {
                name.contains("Team Treasurite") && livingEntity.hasMaxHealth(1000000f) -> HollowsStructure.CORLEONE
                name.contains("Key Guardian") -> HollowsStructure.KEY_GUARDIAN
                else -> null
            } ?: return@listenEvent

            if (structure.isWaypointEnabled()) HollowsModule.waypoints[structure.internalWaypointName] = Waypoint(
                Component.literal(structure.displayName),
                entity.position(),
                WaypointType.OUTLINE_WITH_BEAM,
                structure.waypointColor
            )
        }
    }
}