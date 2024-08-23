package dev.nyon.skylper.skyblock.models.mining

import dev.nyon.skylper.skyblock.models.mining.crystalHollows.Crystal
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalInstance
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState
import kotlinx.serialization.Serializable

@Serializable
data class HeartOfTheMountain(
    var crystals: List<CrystalInstance> = Crystal.entries.map {
        CrystalInstance(it, CrystalState.NOT_FOUND)
    },
    var currentMithrilPowder: Int = 0,
    var currentGemstonePowder: Int = 0,
    var currentGlacitePowder: Int = 0,
    var totalMithrilPowder: Int = 0,
    var totalGemstonePowder: Int = 0,
    var totalGlacitePowder: Int = 0,
    var pickaxeAbility: String? = null,
    var peakOfTheMountainLevel: Int = 0,
    var skyMall: Boolean = false,
    var powderBuffLevel: Int = 0
)