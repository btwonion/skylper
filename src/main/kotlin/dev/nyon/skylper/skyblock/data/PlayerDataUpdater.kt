package dev.nyon.skylper.skyblock.data

import dev.nyon.skylper.extensions.CrystalFoundEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.ProfileChangeEvent

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
            playerData.currentProfile?.crystalHollows?.foundCrystals?.add(crystal)
        }
    }
}