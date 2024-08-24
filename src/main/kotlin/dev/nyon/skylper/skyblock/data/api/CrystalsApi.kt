package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.models.Area
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CreationReason
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.Crystal
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.HollowsLocation

object CrystalsApi {
    val crystalStates get() = HeartOfTheMountainApi.data.crystals

    private val crystalFoundRegex get() = regex("chat.hollows.run.crystalFound")
    private val crystalPlacedRegex get() = regex("chat.hollows.run.crystalPlaced")

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun messageEvent(event: MessageEvent) {
        event.rawText.checkFoundCrystal()
        event.rawText.checkPlacedCrystal()
    }

    private fun String.checkFoundCrystal() {
        val match = crystalFoundRegex.singleGroup(this) ?: return
        val foundCrystal = Crystal.entries.find { it.displayName == match } ?: return

        HeartOfTheMountainApi.data.crystals.find { it.crystal == foundCrystal }?.state = CrystalState.FOUND
        invokeEvent(CrystalFoundEvent(foundCrystal))

        val location = foundCrystal.associatedLocationSpecific() ?: return
        invokeEvent(
            LocatedHollowsStructureEvent(
                HollowsLocation(minecraft.player!!.position(), CreationReason.CRYSTAL, location)
            )
        )
    }

    private fun String.checkPlacedCrystal() {
        val crystal =
            crystalPlacedRegex.singleGroup(this).run { Crystal.entries.find { it.displayName == this } } ?: return
        HeartOfTheMountainApi.data.crystals.find { it.crystal == crystal }?.state = CrystalState.PLACED
        invokeEvent(CrystalPlaceEvent(crystal))
    }
}