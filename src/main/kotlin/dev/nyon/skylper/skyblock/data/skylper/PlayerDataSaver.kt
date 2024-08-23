package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.ProfileChangeEvent
import dev.nyon.skylper.mcScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

object PlayerDataSaver {
    @Suppress("unused")
    val profileListener = listenInfoEvent<ProfileChangeEvent> {
        if (playerData.profiles.containsKey(next)) return@listenInfoEvent
        playerData.profiles[next ?: return@listenInfoEvent] = ProfileData()
    }

    fun startSaveTask() {
        mcScope.launch {
            while (true) {
                delay(30.seconds)
                saveConfig(playerData)
            }
        }
    }
}
