package dev.nyon.skylper.config

import dev.nyon.skylper.extensions.ColorSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.fabricmc.loader.api.FabricLoader
import java.awt.Color
import java.nio.file.Path

val configJsonBuilder: JsonBuilder.() -> Unit = {
    serializersModule = SerializersModule {
        contextual(ColorSerializer)
    }
}
val configDir: Path = FabricLoader.getInstance().configDir.resolve("skylper")
var config: Config = Config()

@Serializable
data class Config(val crystalHollows: CrystalHollowsConfig = CrystalHollowsConfig()) {
    @Serializable
    data class CrystalHollowsConfig(
        val showWaypoints: Boolean = true,
        val showJungleTempleCrystalWaypoint: Boolean = true,
        val parseLocationChats: Boolean = true,
        val highlightChests: Boolean = true,
        val chestHighlightColor: @Contextual Color = Color(255, 0, 0)
    )
}