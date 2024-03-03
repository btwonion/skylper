package dev.nyon.skylper.skyblock.data.online

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

abstract class SkyblockOnlineData<T> {
    companion object {
        const val SKYBLOCK_API_URL = "https://api.hypixel.net/v2/resources/skyblock/"
        val hypixelApiJson = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        val httpClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(hypixelApiJson)
            }
        }
    }

    abstract val path: String
    suspend inline fun <reified D : T> refresh() {
        val result = runCatching {
            httpClient.get("$SKYBLOCK_API_URL$path").body<D>()
        }
        setData(result.getOrNull())
    }

    abstract fun setData(data: T?)
}