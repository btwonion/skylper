package dev.nyon.skylper.skyblock.data.online

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

abstract class SkyblockOnlineData<T : Any>(val kClass: KClass<T>) {
    companion object {
        const val SKYBLOCK_API_URL = "https://api.hypixel.net/v2/resources/skyblock/"
        val hypixelApiJson =
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        val httpClient =
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(hypixelApiJson)
                }
            }

        val data: List<SkyblockOnlineData<*>> = listOf(MayorData)

        suspend fun init() {
            data.forEach {
                it.refresh()
            }
        }
    }

    abstract val path: String

    @OptIn(InternalSerializationApi::class)
    suspend inline fun refresh() {
        val result =
            runCatching {
                val result = httpClient.get("$SKYBLOCK_API_URL$path").bodyAsText()
                hypixelApiJson.decodeFromString(kClass.serializer(), result)
            }
        setData(result.getOrNull())
    }

    abstract fun setData(data: T?)
}
