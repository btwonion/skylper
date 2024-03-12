package dev.nyon.skylper.skyblock

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.nyon.skylper.config.screen.createYaclScreen
import dev.nyon.skylper.extensions.command.executeAsync
import dev.nyon.skylper.extensions.command.sub
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.appendCrystalHollowsSubCommand
import kotlinx.coroutines.delay
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import kotlin.time.Duration.Companion.milliseconds

fun registerRootCommand() {
    ClientCommandRegistrationCallback.EVENT.register(
        ClientCommandRegistrationCallback { dispatcher, _ ->
            dispatcher.register(
                LiteralArgumentBuilder.literal<FabricClientCommandSource>("skylper").also { root ->
                    root.openConfigGui()
                    root.appendCrystalHollowsSubCommand()

                    root.sub("gui") { gui ->
                        gui.openConfigGui()
                    }
                }
            )
        }
    )
}

private fun LiteralArgumentBuilder<FabricClientCommandSource>.openConfigGui() {
    executeAsync {
        delay(100.milliseconds)
        val screen = createYaclScreen(null)
        minecraft.setScreen(screen)
    }
}
