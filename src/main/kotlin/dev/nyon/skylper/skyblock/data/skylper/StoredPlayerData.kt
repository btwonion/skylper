package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.cooldowns.MiningAbility
import dev.nyon.skylper.skyblock.models.mining.HeartOfTheMountain
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.Crystal
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalInstance
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState
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
            if (PlayerSessionData.isOnSkyblock && now - lastProfileNotice > 1.minutes) println("No active Skyblock profile found. Please relog into Skyblock.")
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
data class ProfileData(val heartOfTheMountain: HeartOfTheMountain = HeartOfTheMountain())