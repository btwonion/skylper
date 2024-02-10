package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.hollows.Crystal
import kotlinx.serialization.Serializable

lateinit var playerData: StoredPlayerData

@Serializable
data class StoredPlayerData(
    val profiles: MutableMap<String, ProfileData> = mutableMapOf()
)

@Serializable
data class ProfileData(val crystalHollows: CrystalHollows = CrystalHollows())

@Serializable
data class CrystalHollows(val foundCrystals: MutableSet<Crystal> = mutableSetOf())