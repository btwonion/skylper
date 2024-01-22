package dev.nyon.skylper.extensions.neu

import dev.nyon.skylper.Skylper.ktorClient
import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.NeuReloadEvent
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

object NeuDownloader {
    private const val REMOTE_REPO = "NotEnoughUpdates/NotEnoughUpdates-REPO"
    private val filesToDownload = listOf("constants/islands.json", "constants/zones.json")

    /**
     * Stores the raw content of the files.
     * Path to Content
     */
    private var rawFiles = mapOf<String, String>()

    suspend fun reloadFiles() {
        rawFiles = filesToDownload.associateWith { path ->
            val jsonObject =
                ktorClient.get("https://api.github.com/repos/$REMOTE_REPO/contents/path").body<JsonObject>()
            val contentString = jsonObject["content"]?.jsonPrimitive?.contentOrNull
                ?: error("Failed to load '$path' from NEU repository")
            contentString
        }

        EventHandler.invokeEvent(NeuReloadEvent(rawFiles))
    }
}