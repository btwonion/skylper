package dev.nyon.skylper.extensions

import net.fabricmc.loader.api.FabricLoader

fun debug(string: String) {
    if (FabricLoader.getInstance().isDevelopmentEnvironment) println("[skylper] $string")
}