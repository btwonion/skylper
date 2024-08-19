package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.PowderUpdateEvent
import dev.nyon.skylper.skyblock.models.mining.SkyMallPerk

object PowderApi {

    fun getPowderMultiplier(type: PowderUpdateEvent.PowderType): Double {
        var multiplier = 1.0
        multiplier += HeartOfTheMountainApi.data.powderBuffLevel.toFloat() / 100.0

        // Missing: scatha, glacite golem, mithril golem, ruby/gemstone/topaz/jasper drill

        return multiplier
    }
}