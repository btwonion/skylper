package dev.nyon.skylper.config

import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader

val configDir = FabricLoader.getInstance().configDir.resolve("skylper")
var config: Config = Config()

@Serializable
class Config