package dev.nyon.skylper

import com.mojang.blaze3d.platform.InputConstants
import dev.nyon.konfig.config.config
import dev.nyon.konfig.config.loadConfig
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.configDir
import dev.nyon.skylper.config.configJsonBuilder
import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.FabricEvents
import dev.nyon.skylper.extensions.MinecraftStopEvent
import dev.nyon.skylper.skyblock.Mining
import dev.nyon.skylper.skyblock.data.online.OnlineData
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.PlayerDataSaver
import dev.nyon.skylper.skyblock.data.skylper.PlayerDataUpdater
import dev.nyon.skylper.skyblock.data.skylper.StoredPlayerData
import dev.nyon.skylper.skyblock.data.skylper.playerData
import dev.nyon.skylper.skyblock.menu.Menu
import dev.nyon.skylper.skyblock.misc.Misc
import dev.nyon.skylper.skyblock.registerRootCommand
import dev.nyon.skylper.skyblock.render.SkylperHud
import kotlinx.coroutines.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import dev.nyon.skylper.config.config as internalConfig

lateinit var mcDispatcher: CoroutineDispatcher
lateinit var mcScope: CoroutineScope
lateinit var minecraft: Minecraft
val independentScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

object Skylper : ClientModInitializer {
    val crystalHollowsLocationKeybinding: KeyMapping = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            Component.translatable("menu.skylper.keybinding.location_screen").string,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "skylper"
        )
    )

    @Suppress("SpellCheckingInspection")
    override fun onInitializeClient() {
        minecraft = Minecraft.getInstance()
        mcDispatcher = minecraft.asCoroutineDispatcher()
        mcScope = CoroutineScope(SupervisorJob() + mcDispatcher)

        handleStop()

        config(
            configDir.resolve("skylper.json"), 1, Config(), configJsonBuilder::invoke
        ) { _, _ -> null }
        internalConfig = loadConfig<Config>()

        config(
            configDir.resolve("playerdata.json"), 1, StoredPlayerData(), configJsonBuilder::invoke
        ) { _, _ -> null }
        playerData = loadConfig<StoredPlayerData>()

        mcScope.launch { setup() }
    }

    private suspend fun setup() {
        PlayerSessionData.startUpdaters()
        PlayerDataUpdater.initUpdaters()
        PlayerDataSaver.startSaveTask()
        OnlineData.init()
        registerRootCommand()

        FabricEvents.listenForFabricEvents()

        SkylperHud.init()

        Mining.init()
        Menu.init()
        Misc.init()
    }

    private fun handleStop() {
        EventHandler.listenEvent<MinecraftStopEvent, Unit> {
            saveConfig(internalConfig)
            saveConfig(playerData)
        }
    }
}
