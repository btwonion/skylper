package dev.nyon.skylper.skyblock.data.online

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object MayorData : SkyblockOnlineData<HypixelApiMayorResponse>() {
    override val path: String = "election"

    var mayor: Mayor? = null
    override fun setData(data: HypixelApiMayorResponse?) {
        mayor = data?.mayor?.name
    }
}

@Serializable
data class HypixelApiMayorResponse(val success: Boolean, val lastUpdated: Long, val mayor: MayorData) {
    @Serializable
    data class MayorData(val key: String, val name: Mayor, val perks: List<Perk>, val election: Election)

    @Serializable
    data class Perk(val name: String, val description: String)

    @Serializable
    data class Election(val year: Int, val candidates: List<ElectionCandidate>)

    @Serializable
    data class ElectionCandidate(val key: String, val name: String, val perks: List<Perk>, val votes: Int)
}

@Serializable
enum class Mayor {
    @SerialName("Aatrox")
    AATROX,

    @SerialName("Cole")
    COLE,

    @SerialName("Diana")
    DIANA,

    @SerialName("Diaz")
    DIAZ,

    @SerialName("Finnegan")
    FINNEGAN,

    @SerialName("Foxy")
    FOXY,

    @SerialName("Marina")
    MARINA,

    @SerialName("Paul")
    PAUL,

    @SerialName("Jerry")
    JERRY,

    @SerialName("Derpy")
    DERPY,

    @SerialName("Scorpius")
    SCORPIUS
}