package dev.nyon.skylper.skyblock.data.online

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

object Regexes : OnlineData<SkyHanniRegexes>(SkyHanniRegexes::class) {
    override val url: String = SKYHANNI_REPO_URL
    override val path: String = "regexes.json"

    var regexes: MutableMap<String, Regex> = mutableMapOf()

    override fun setData(data: SkyHanniRegexes?) {
        regexes.putAll(data?.regexes ?: return)
    }
}

object AdditionalRegexes : OnlineData<SkylperRegexes>(SkylperRegexes::class) {
    override val url: String = SKYLPER_REPO_URL
    override val path: String = "regexes.json"

    override fun setData(data: SkylperRegexes?) {
        Regexes.regexes.putAll(data?.regexes ?: return)
    }
}

@Serializable
data class SkylperRegexes(val regexes: Map<String, @Contextual Regex>)

@Serializable
data class SkyHanniRegexes(val sourceLabel: String, val regexes: Map<String, @Contextual Regex>)