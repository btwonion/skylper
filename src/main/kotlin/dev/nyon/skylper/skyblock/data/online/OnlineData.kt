package dev.nyon.skylper.skyblock.data.online

import dev.nyon.skylper.extensions.httpClient
import dev.nyon.skylper.extensions.json
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

abstract class OnlineData<T : Any>(val kClass: KClass<T>) {
    companion object {
        const val SKYBLOCK_API_URL = "https://api.hypixel.net/v2/resources/skyblock/"
        const val SKYLPER_REPO_URL = "https://raw.githubusercontent.com/btwonion/skylper/regexes/constants/" // TODO: change this back to master

        val data: List<OnlineData<*>> = listOf(MayorData, Regexes, ToolGroups, IslandGroups, Locations)
    }

    abstract val url: String
    abstract val path: String

    @OptIn(InternalSerializationApi::class)
    suspend inline fun refresh() {
        val result = runCatching {
            val result = httpClient.get("$url$path").bodyAsText()
            json.decodeFromString(kClass.serializer(), result)
        }
        setData(result.getOrNull())
    }

    abstract fun setData(data: T?)
}
