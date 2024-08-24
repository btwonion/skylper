package dev.nyon.skylper.skyblock.models

import dev.nyon.skylper.extensions.regex
import kotlinx.serialization.Serializable

@Serializable
enum class Area {
    EVERYWHERE,
    PRIVATE_ISLAND,
    GARDEN,
    HUB,
    THE_FARMING_ISLANDS,
    THE_PARK,
    SPIDERS_DEN,
    THE_END,
    CRIMSON_ISLE,
    GOLD_MINE,
    DEEP_CAVERNS,
    DWARVEN_MINES,
    CRYSTAL_HOLLOWS,
    MINESHAFT;

    fun getRegex(): Regex {
        return regex("tablist.area.${this.name.lowercase()}")
    }
}