package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.extensions.CrystalFoundEvent
import dev.nyon.skylper.extensions.CrystalPlaceEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.NucleusRunCompleteEvent
import dev.nyon.skylper.extensions.ProfileChangeEvent
import dev.nyon.skylper.skyblock.hollows.CrystalState

object PlayerDataUpdater {
    fun initUpdaters() {
        profileUpdateChecker()
        crystalHollowsChecker()
    }

    private fun profileUpdateChecker() {
        listenEvent<ProfileChangeEvent> { (_, nextProfile) ->
            if (playerData.profiles.containsKey(nextProfile)) return@listenEvent
            playerData.profiles[nextProfile ?: return@listenEvent] = ProfileData()
        }
    }

    private fun crystalHollowsChecker() {
        listenEvent<CrystalFoundEvent> { (crystal) ->
            playerData.currentProfile?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state = CrystalState.FOUND
        }

        listenEvent<NucleusRunCompleteEvent> {
            playerData.currentProfile?.crystalHollows?.crystals?.forEach { it.state = CrystalState.NOT_FOUND }
        }

        listenEvent<CrystalPlaceEvent> { (crystal) ->
            playerData.currentProfile?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state = CrystalState.PLACED
        }
    }
}