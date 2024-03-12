package dev.nyon.skylper

import dev.nyon.konfig.config.config
import dev.nyon.konfig.config.loadConfig
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.configDir
import dev.nyon.skylper.config.configJsonBuilder
import dev.nyon.skylper.config.migrate
import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.FabricEvents
import dev.nyon.skylper.extensions.MinecraftStopEvent
import dev.nyon.skylper.skyblock.Mining
import dev.nyon.skylper.skyblock.data.online.SkyblockOnlineData
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.PlayerDataSaver
import dev.nyon.skylper.skyblock.data.skylper.PlayerDataUpdater
import dev.nyon.skylper.skyblock.data.skylper.StoredPlayerData
import dev.nyon.skylper.skyblock.data.skylper.migrateStoredPlayerData
import dev.nyon.skylper.skyblock.data.skylper.playerData
import dev.nyon.skylper.skyblock.menu.Menu
import dev.nyon.skylper.skyblock.registerRootCommand
import dev.nyon.skylper.skyblock.render.SkylperHud
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.Minecraft
import dev.nyon.skylper.config.config as internalConfig

lateinit var mcDispatcher: CoroutineDispatcher
lateinit var mcScope: CoroutineScope
lateinit var minecraft: Minecraft
val independentScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

object Skylper : ClientModInitializer {
    @Suppress("SpellCheckingInspection")
    override fun onInitializeClient() {
        minecraft = Minecraft.getInstance()
        mcDispatcher = minecraft.asCoroutineDispatcher()
        mcScope = CoroutineScope(SupervisorJob() + mcDispatcher)

        handleStop()

        config(
            configDir.resolve("skylper.json"),
            1,
            Config(),
            configJsonBuilder::invoke
        ) { jsonTree, version -> migrate(jsonTree, version) }
        internalConfig = loadConfig<Config>() ?: error("No config settings provided to load config!")

        config(
            configDir.resolve("playerdata.json"),
            1,
            StoredPlayerData(),
            configJsonBuilder::invoke
        ) { jsonTree, version ->
            migrateStoredPlayerData(jsonTree, version)
        }
        playerData = loadConfig<StoredPlayerData>() ?: error("No config settings provided to load player data!")

        mcScope.launch { setup() }
    }

    private suspend fun setup() {
        PlayerSessionData.startUpdaters()
        PlayerDataUpdater.initUpdaters()
        PlayerDataSaver.startSaveTask()
        SkyblockOnlineData.init()
        registerRootCommand()

        FabricEvents.listenForFabricEvents()

        SkylperHud.init()

        Mining.init()
        Menu.init()
    }

    private fun handleStop() {
        EventHandler.listenEvent<MinecraftStopEvent, Unit> {
            saveConfig(internalConfig)
            saveConfig(playerData)
        }
    }
}
