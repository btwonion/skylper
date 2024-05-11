package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

object NameTagEntityListener {
    @Suppress("unused")
    val tickEvent = listenEvent<TickEvent, Unit> { _ ->
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        var firstGuardianLocation: Vec3? = null

        minecraft.level?.entities?.get(minecraft.player!!.radiusBox(50)) { entity ->
            if (entity as? LivingEntity == null) return@get
            val name = entity.displayName?.string
            val customName = entity.customName?.string
            val entityPos = entity.position()

            var override = false
            val location: HollowsLocation? = when {
                name?.contains("King Yolkar") == true -> HollowsLocation(
                    entityPos, PreDefinedHollowsLocationSpecific.GOBLIN_KING
                )
                name?.contains("Professor Robot") == true -> HollowsLocation(
                    entityPos.add(-16.0, 5.0, 21.0), PreDefinedHollowsLocationSpecific.PRECURSOR_CITY
                )
                name?.contains("Kalhuiki Door Guardian") == true -> {
                    if (firstGuardianLocation == null) {
                        firstGuardianLocation = entityPos
                        null
                    } else {
                        val leftGuardian = listOf(firstGuardianLocation!!, entityPos).minByOrNull { it.x }!!
                        val crystalPos = leftGuardian.add(61.0, -44.0, 18.0)
                        HollowsLocation(crystalPos, PreDefinedHollowsLocationSpecific.JUNGLE_TEMPLE)
                    }
                }
                name?.contains("Odawa") == true -> HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.ODAWA)
                customName?.contains("Boss Corleone") == true && entity.hasMaxHealth(1_000_000f) -> {
                    override = true
                    HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.CORLEONE)
                }
                customName?.contains("Key Guardian") == true -> {
                    override = true
                    HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.KEY_GUARDIAN)
                }
                customName?.contains("Bal") == true && entity.type == EntityType.MAGMA_CUBE -> HollowsLocation(
                    entityPos, PreDefinedHollowsLocationSpecific.KHAZAD_DUM
                )

                else -> null
            }

            if (location != null) EventHandler.invokeEvent(LocatedHollowsStructureEvent(location, override))
        }
    }
}
