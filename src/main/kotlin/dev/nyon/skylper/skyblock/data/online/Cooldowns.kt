package dev.nyon.skylper.skyblock.data.online

import kotlinx.serialization.Serializable

object Cooldowns : OnlineData<SkylperCooldowns>(SkylperCooldowns::class) {
    override val url: String = SKYLPER_REPO_URL
    override val path: String = "regexes.json"

    var cooldowns: SkylperCooldowns = SkylperCooldowns(mapOf())

    override fun setData(data: SkylperCooldowns?) {
        cooldowns = data ?: return
    }
}

@Serializable
data class SkylperCooldowns(val mining: Map<String, List<Int>>)