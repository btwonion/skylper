package dev.nyon.skylper.config

import dev.nyon.skylper.extensions.ColorSerializer
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import java.awt.Color
import java.nio.file.Path

val configDir: Path = FabricLoader.getInstance().configDir.resolve("skylper")
var config: Config = Config()

@Serializable
data class Config(val crystalHollows: CrystalHollowsConfig = CrystalHollowsConfig()) {
    @Serializable
    data class CrystalHollowsConfig(
        val showWaypoints: Boolean = true,
        val parseLocationChats: Boolean = true,
        val highlightChests: Boolean = true,
        val highlightChestsThroughWalls: Boolean = true,
        val chestHighlightColor: @Serializable(with = ColorSerializer::class) Color = Color(255, 0, 0)
    )
}