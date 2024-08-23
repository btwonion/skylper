package dev.nyon.skylper.skyblock.data.online

import dev.nyon.skylper.extensions.sentWarnings
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

object Regexes : OnlineData<SkylperRegexes>(SkylperRegexes::class) {
    override val url: String = SKYLPER_REPO_URL
    override val path: String = "regexes.json"

    var regexes: MutableMap<String, Regex> = mutableMapOf()

    override fun setData(data: SkylperRegexes?) {
        sentWarnings.clear()
        regexes.putAll(data?.regexes ?: return)
    }
}

@Serializable
data class SkylperRegexes(val regexes: Map<String, @Contextual Regex>)