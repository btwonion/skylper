package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.MiningAbility
import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.CrystalInstance
import dev.nyon.skylper.skyblock.mining.hollows.CrystalState
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
data class ProfileData(val mining: Mining = Mining())

@Serializable
data class Mining(
    var peakOfTheMountain: Int = 0,
    var abilityLevel: Int = 1,
    var selectedAbility: MiningAbility? = null,
    var mithrilPowder: Int = 0,
    var gemstonePowder: Int = 0,
    var glacitePowder: Int = 0,
    val crystalHollows: CrystalHollows = CrystalHollows()
)

@Serializable
data class CrystalHollows(
    val crystals: List<CrystalInstance> = Crystal.entries.map {
        CrystalInstance(
            it, CrystalState.NOT_FOUND
        )
    }
)
