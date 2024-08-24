package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.extensions.event.ProfileChangeEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.mcScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

object PlayerDataSaver {
    @SkylperEvent
    fun profileChangeEvent(event: ProfileChangeEvent) {
        if (playerData.profiles.containsKey(event.next)) return
        playerData.profiles[event.next ?: return] = ProfileData()
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
