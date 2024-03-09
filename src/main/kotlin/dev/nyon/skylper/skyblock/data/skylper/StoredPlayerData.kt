package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.mining.MiningAbility
import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.CrystalInstance
import dev.nyon.skylper.skyblock.mining.hollows.CrystalState
import kotlinx.serialization.Serializable

lateinit var playerData: StoredPlayerData

@Serializable
data class StoredPlayerData(
    val profiles: MutableMap<String, ProfileData> = mutableMapOf()
)

@Serializable
data class ProfileData(val mining: Mining = Mining())

@Serializable
data class Mining(
    val abilityLevel: Int = 1,
    var selectedAbility: MiningAbility? = null,
    val crystalHollows: CrystalHollows = CrystalHollows()
)

@Serializable
data class CrystalHollows(
    val crystals: List<CrystalInstance> = Crystal.entries.map {
        CrystalInstance(
            it, CrystalState.NOT_FOUND
        )
    }
)