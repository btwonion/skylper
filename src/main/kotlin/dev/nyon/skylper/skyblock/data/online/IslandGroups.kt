package dev.nyon.skylper.skyblock.data.online

import dev.nyon.skylper.skyblock.models.Area
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object IslandGroups : OnlineData<IslandGroupsData>(IslandGroupsData::class) {
    override val url: String = SKYLPER_REPO_URL
    override val path: String = "island_groups.json"

    var groups: IslandGroupsData = IslandGroupsData(listOf(), listOf())

    override fun setData(data: IslandGroupsData?) {
        groups = data ?: return
    }
}

@Serializable
data class IslandGroupsData(val mining: List<Area>, @SerialName("mining_events") val miningEvents: List<Area>)