package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.mcScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

object PlayerDataSaver {
    fun startSaveTask() {
        mcScope.launch {
            while (true) {
                delay(30.seconds)
                saveConfig(playerData)
            }
        }
    }
}
