package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.PowderUpdateEvent
import dev.nyon.skylper.extensions.internalName
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.models.Rarity

object PowderApi {

    // This function does not consider glacite powder yet. Atm only used for Powder Grinding calculation.
    fun getPowderMultiplier(type: PowderUpdateEvent.PowderType): Double {
        var multiplier = 1.0
        multiplier += HeartOfTheMountainApi.data.powderBuffLevel.toFloat() / 100.0

        val currentPet = PetApi.currentPet
        when (currentPet?.name) {
            "Scatha" -> {
                if (currentPet.rarity == Rarity.LEGENDARY && type == PowderUpdateEvent.PowderType.GEMSTONE) multiplier += currentPet.level / 500.0
            }

            "Mithril Golem" -> {
                when (currentPet.rarity) {
                    Rarity.RARE -> multiplier += currentPet.level / 1000.0
                    Rarity.EPIC, Rarity.LEGENDARY -> multiplier += currentPet.level / 500.0
                    else -> {}
                }
            }
        }

        val heldItem = minecraft.player?.useItem
        if (heldItem != null) {
            val internalName = heldItem.internalName

            when (internalName) {
                "GEMSTONE_DRILL_1" -> multiplier += 0.05
                "GEMSTONE_DRILL_2" -> multiplier += 0.1
                "GEMSTONE_DRILL_3" -> multiplier += 0.15
                "GEMSTONE_DRILL_4" -> multiplier += 0.2
            }
        }

        return multiplier
    }
}