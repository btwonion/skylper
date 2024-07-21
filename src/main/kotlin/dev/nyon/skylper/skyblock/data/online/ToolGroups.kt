package dev.nyon.skylper.skyblock.data.online

import kotlinx.serialization.Serializable

object ToolGroups : OnlineData<ToolGroupsData>(ToolGroupsData::class) {
    override val url: String = SKYLPER_REPO_URL
    override val path: String = "tool_groups.json"

    var groups: ToolGroupsData = ToolGroupsData(listOf())

    override fun setData(data: ToolGroupsData?) {
        groups = data ?: return
    }
}

@Serializable
data class ToolGroupsData(val mining: List<String>)