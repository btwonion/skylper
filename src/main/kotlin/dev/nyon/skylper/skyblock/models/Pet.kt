package dev.nyon.skylper.skyblock.models

import kotlinx.serialization.Serializable

@Serializable
data class Pet(val name: String, var level: Int, val rarity: Rarity, var enabled: Boolean)
