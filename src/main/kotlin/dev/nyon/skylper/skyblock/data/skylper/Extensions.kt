package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.data.session.PlayerSessionData

val StoredPlayerData.currentProfile: ProfileData?
    get() {
        return this.profiles[PlayerSessionData.profile ?: return null]
    }