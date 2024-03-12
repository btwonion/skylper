package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.mcScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

object PlayerDataSaver {
    fun startSaveTask() {
        mcScope.launch {
            while (true) {
                delay(1.minutes)
                saveConfig(playerData)
            }
        }
    }
}
