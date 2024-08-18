package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CreationReason
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.Crystal
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.HollowsLocation

object CrystalsApi {
    val crystalStates get() = currentProfile.mining.crystalHollows.crystals

    private val crystalFoundRegex get() = regex("chat.hollows.run.crystalFound")
    private val crystalPlacedRegex get() = regex("chat.hollows.run.crystalPlaced")

    fun init() = listenInfoEvent<MessageEvent> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent

        rawText.checkFoundCrystal()
        rawText.checkPlacedCrystal()
    }

    private fun String.checkFoundCrystal() {
        val match = crystalFoundRegex.singleGroup(this) ?: return
        val foundCrystal = Crystal.entries.find { it.displayName == match } ?: return

        currentProfile.mining.crystalHollows.crystals.find { it.crystal == foundCrystal }?.state = CrystalState.FOUND
        EventHandler.invokeEvent(CrystalFoundEvent(foundCrystal))

        val location = foundCrystal.associatedLocationSpecific() ?: return
        EventHandler.invokeEvent(
            LocatedHollowsStructureEvent(
                HollowsLocation(minecraft.player!!.position(), CreationReason.CRYSTAL, location)
            )
        )
    }

    private fun String.checkPlacedCrystal() {
        val crystal =
            crystalPlacedRegex.singleGroup(this).run { Crystal.entries.find { it.displayName == this } } ?: return
        currentProfile.mining.crystalHollows.crystals.find { it.crystal == crystal }?.state = CrystalState.PLACED
        EventHandler.invokeEvent(CrystalPlaceEvent(crystal))
    }
}