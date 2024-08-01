package dev.nyon.skylper.extensions

import dev.nyon.skylper.config.configJsonBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    configJsonBuilder()
}

val httpClient = HttpClient(CIO)
