package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.models.Pet
import dev.nyon.skylper.skyblock.models.mining.HeartOfTheMountain
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.minutes

lateinit var playerData: StoredPlayerData
private var lastProfileNotice: Instant = Clock.System.now()
val currentProfile: ProfileData
    get() {
        val profile = playerData.profiles[PlayerSessionData.profile]
        if (profile == null) {
            val now = Clock.System.now()
            if (PlayerSessionData.isOnSkyblock && now - lastProfileNotice > 1.minutes) println("No active Skyblock profile found. Please relog onto Skyblock.")
            lastProfileNotice = now
            return ProfileData()
        }
        return profile
    }

@Serializable
data class StoredPlayerData(
    val profiles: MutableMap<String, ProfileData> = mutableMapOf()
)

@Serializable
data class ProfileData(
    val pets: MutableList<Pet> = mutableListOf(), val heartOfTheMountain: HeartOfTheMountain = HeartOfTheMountain()
)