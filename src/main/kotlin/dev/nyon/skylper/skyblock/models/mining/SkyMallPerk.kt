package dev.nyon.skylper.skyblock.models.mining

import dev.nyon.skylper.extensions.regex

enum class SkyMallPerk(private val regexKey: String) {
    MINING_SPEED("mining_speed"),
    MINING_FORTUNE("mining_fortune"),
    EXTRA_POWDER("extra_powder"),
    REDUCED_COOLDOWN("reduced_cooldown"),
    MORE_GOBLINS("more_goblins"),
    MORE_TITANIUM("more_titanium");

    fun getRegex(): Regex {
        return regex("chat.mining.skymall.$regexKey")
    }

    companion object {
        fun byMessage(message: String): SkyMallPerk? {
            return entries.find { it.getRegex().matches(message) }
        }
    }
}