package dev.nyon.skylper

import dev.nyon.konfig.config.config
import dev.nyon.konfig.config.loadConfig
import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.configDir
import dev.nyon.skylper.config.migrate
import dev.nyon.skylper.extensions.FabricEvents
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.*
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.Minecraft
import dev.nyon.skylper.config.config as internalConfig

lateinit var mcDispatcher: CoroutineDispatcher
lateinit var mcScope: CoroutineScope
lateinit var minecraft: Minecraft
val independentScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

object Skylper : ClientModInitializer {
    val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    @Suppress("SpellCheckingInspection")
    override fun onInitializeClient() {
        minecraft = Minecraft.getInstance()
        mcDispatcher = minecraft.asCoroutineDispatcher()
        mcScope = CoroutineScope(SupervisorJob() + mcDispatcher)

        config(configDir.resolve("skylper.json"), 1, Config()) { jsonTree, version -> migrate(jsonTree, version) }
        internalConfig = loadConfig<Config>() ?: error("No config settings provided to load config!")

        config(configDir.resolve("playerdata.json"), 1, StoredPlayerData()) { jsonTree, version ->
            migrateStoredPlayerData(jsonTree, version)
        }
        playerData = loadConfig<StoredPlayerData>() ?: error("No config settings provided to load player data!")

        mcScope.launch { setup() }
    }

    private fun setup() { //NeuDownloader.reloadFiles() Not needed at the moment
        PlayerSessionData.startUpdaters()
        PlayerDataUpdater.initUpdaters()
        PlayerDataSaver.startSaveTask()

        FabricEvents.listenForFabricEvents()

        HollowsModule.init()
    }
}