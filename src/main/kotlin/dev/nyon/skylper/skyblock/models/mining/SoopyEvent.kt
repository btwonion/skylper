package dev.nyon.skylper.skyblock.models.mining

import dev.nyon.skylper.extensions.InstantMillisSerializer
import dev.nyon.skylper.skyblock.models.Area
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SoopyEvent(val success: Boolean, val data: Data) {
    @Serializable
    data class Data(
        @SerialName("running_events")
        val runningEvents: Map<Area, List<MiningEvent>>
    )

    @Serializable
    data class MiningEvent(
        val event: MiningEventType,
        @SerialName("ends_at")
        val endsAt: @Serializable(with = InstantMillisSerializer::class) Instant,
        @SerialName("lobby_count")
        val lobbyCount: Int
    )
}
