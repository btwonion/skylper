package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.hollows.Crystal
import dev.nyon.skylper.skyblock.hollows.CrystalInstance
import dev.nyon.skylper.skyblock.hollows.CrystalState
import kotlinx.serialization.Serializable

lateinit var playerData: StoredPlayerData

@Serializable
data class StoredPlayerData(
    val profiles: MutableMap<String, ProfileData> = mutableMapOf()
)

@Serializable
data class ProfileData(val crystalHollows: CrystalHollows = CrystalHollows())

@Serializable
data class CrystalHollows(
    val crystals: List<CrystalInstance> = Crystal.entries.map {
        CrystalInstance(
            it, CrystalState.NOT_FOUND
        )
    }
)