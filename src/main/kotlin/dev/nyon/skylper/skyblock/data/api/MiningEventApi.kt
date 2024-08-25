package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.httpClient
import dev.nyon.skylper.extensions.json
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.models.mining.SoopyEvent
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

object MiningEventApi {
    private var soopyEventData: SoopyEvent? = null
    val currentEvents: List<SoopyEvent.MiningEvent>
        get() = soopyEventData?.data?.runningEvents?.get(PlayerSessionData.currentArea) ?: emptyList()

    @Suppress("unused")
    private val updater = independentScope.launch {
        while (true) {
            val soopyEvent = runCatching {
                val response = httpClient.get("https://api.soopy.dev/skyblock/chevents/get").bodyAsText()
                json.decodeFromString<SoopyEvent>(response)
            }.onFailure { it.printStackTrace() }.getOrNull()
            soopyEventData = soopyEvent
            delay(45.seconds)
        }
    }
}