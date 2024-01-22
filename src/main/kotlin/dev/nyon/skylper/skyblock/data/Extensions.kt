package dev.nyon.skylper.skyblock.data

import dev.nyon.skylper.skyblock.PlayerSessionData

val StoredPlayerData.currentProfile: ProfileData?
    get() {
        return this.profiles[PlayerSessionData.profile ?: return null]
    }