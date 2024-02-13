package dev.nyon.skylper.skyblock

import dev.nyon.skylper.config.screen.createYaclScreen
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.appendCrystalHollowsSubCommand
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.silkmc.silk.commands.clientCommand
import kotlin.time.Duration.Companion.milliseconds

fun registerRootCommand() = clientCommand("skylper") {
    appendCrystalHollowsSubCommand()

    literal("gui") {
        runs {
            mcScope.launch {
                delay(100.milliseconds)
                val screen = createYaclScreen(null)
                minecraft.setScreen(screen)
            }
        }
    }
}