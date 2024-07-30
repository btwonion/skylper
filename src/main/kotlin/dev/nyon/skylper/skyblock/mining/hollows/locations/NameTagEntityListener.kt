package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.EventHandler
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.event.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.event.TickEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

object NameTagEntityListener {
    private val yolkarRegex = regex("nametag.hollows.yolkar")
    private val professorRegex = regex("nametag.hollows.professor_robot")
    private val doorGuardianRegex  = regex("nametag.hollows.door_guardian")
    private val odawaRegex  = regex("nametag.hollows.odawa")
    private val corleoneRegex  = regex("nametag.hollows.corleone")
    private val keyGuardianRegex  = regex("nametag.hollows.key_guardian")
    private val balRegex  = regex("nametag.hollows.bal")

    @Suppress("unused")
    val tickEvent = listenEvent<TickEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        var firstGuardianLocation: Vec3? = null

        minecraft.level?.entities?.get(minecraft.player!!.radiusBox(50)) { entity ->
            if (entity as? LivingEntity == null) return@get
            val name = entity.displayName?.string
            val customName = entity.customName?.string
            val entityPos = entity.position()

            var override = false
            val location: HollowsLocation? = when {
                yolkarRegex.matches(name ?: "") -> HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.GOBLIN_KING)
                professorRegex.matches(name ?: "") -> HollowsLocation(
                    entityPos.add(-16.0, 5.0, 21.0), PreDefinedHollowsLocationSpecific.PRECURSOR_CITY
                )
                doorGuardianRegex.matches(name ?: "") -> {
                    if (firstGuardianLocation == null) {
                        firstGuardianLocation = entityPos
                        null
                    } else {
                        val leftGuardian = listOf(firstGuardianLocation!!, entityPos).minByOrNull { it.x }!!
                        val crystalPos = leftGuardian.add(61.0, -44.0, 18.0)
                        HollowsLocation(crystalPos, PreDefinedHollowsLocationSpecific.JUNGLE_TEMPLE)
                    }
                }
                odawaRegex.matches(name ?: "") -> HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.ODAWA)
                corleoneRegex.matches(customName ?: "") && entity.hasMaxHealth(1_000_000f) -> {
                    override = true
                    HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.CORLEONE)
                }
                keyGuardianRegex.matches(customName ?: "") -> {
                    override = true
                    HollowsLocation(entityPos, PreDefinedHollowsLocationSpecific.KEY_GUARDIAN)
                }
                balRegex.matches(customName ?: "") && entity.type == EntityType.MAGMA_CUBE -> HollowsLocation(
                    entityPos, PreDefinedHollowsLocationSpecific.KHAZAD_DUM
                )

                else -> null
            }

            if (location != null) EventHandler.invokeEvent(LocatedHollowsStructureEvent(location, override))
        }
    }
}
