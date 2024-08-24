package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.doubleOrNull
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.internalName
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.models.Rarity
import dev.nyon.skylper.skyblock.models.mining.PowderType

object PowderApi {
    val currentMithrilPowder get() = HeartOfTheMountainApi.data.currentMithrilPowder
    val currentGemstonePowder get() = HeartOfTheMountainApi.data.currentGemstonePowder
    val currentGlacitePowder get() = HeartOfTheMountainApi.data.currentGlacitePowder

    // Only used for Powder Grinding calculation.
    fun getPowderMultiplier(type: PowderType): Double {
        var multiplier = if (CrystalHollowsPowderGrindingApi.doublePowderActive) 2.0 else 1.0
        multiplier += HeartOfTheMountainApi.data.powderBuffLevel.toFloat() / 100.0

        val currentPet = PetApi.currentPet
        when (currentPet?.type) {
            "Scatha" -> {
                if (currentPet.tier == Rarity.LEGENDARY && type == PowderType.GEMSTONE) multiplier += currentPet.level / 500.0
            }

            "Mithril Golem" -> {
                when (currentPet.tier) {
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

    @SkylperEvent
    fun powderGainEvent(event: PowderGainEvent) {
        val amount = event.amount
        when (event.type) {
            PowderType.MITHRIL -> {
                HeartOfTheMountainApi.data.currentMithrilPowder += amount
                HeartOfTheMountainApi.data.totalMithrilPowder += amount
            }

            PowderType.GEMSTONE -> {
                HeartOfTheMountainApi.data.currentGemstonePowder += amount
                HeartOfTheMountainApi.data.totalGemstonePowder += amount
            }

            PowderType.GLACITE -> {
                HeartOfTheMountainApi.data.currentGlacitePowder += amount
                HeartOfTheMountainApi.data.totalGlacitePowder += amount
            }
        }
    }

    @SkylperEvent
    fun tablistUpdateEvent(event: TablistUpdateEvent) { parsePowder(event.cleanLines) }

    @SkylperEvent
    fun sideboardUpdateEvent(event: SideboardUpdateEvent) { parsePowder(event.cleanLines) }

    private val tablistMithrilPowderRegex get() = regex("tablist.mining.mithril")
    private val tablistGemstonePowderRegex get() = regex("tablist.mining.gemstone")
    private val tablistGlacitePowderRegex get() = regex("tablist.mining.glacite")

    private fun parsePowder(lines: List<String>) {
        lines.forEach { line ->
            val mithrilPowder = tablistMithrilPowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val gemstonePowder = tablistGemstonePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val glacitePowder = tablistGlacitePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()

            mithrilPowder?.let { HeartOfTheMountainApi.data.currentMithrilPowder = it }
            gemstonePowder?.let { HeartOfTheMountainApi.data.currentGemstonePowder = it }
            glacitePowder?.let { HeartOfTheMountainApi.data.currentGlacitePowder = it }

            invokeEvent(PowderAdjustedEvent)
        }
    }
}