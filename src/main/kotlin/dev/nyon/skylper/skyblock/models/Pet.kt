package dev.nyon.skylper.skyblock.models

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val type: String,
    var active: Boolean,
    val exp: Double,
    val tier: Rarity,
    val candyUsed: Int,
    var level: Int = 1
)
