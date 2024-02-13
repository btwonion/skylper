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
        val hollowsWaypoints: HollowsWaypoints = HollowsWaypoints(),
        val parseLocationChats: Boolean = true,
        val automaticallyAddLocations: Boolean = true,
        val highlightChests: Boolean = true,
        val chestHighlightColor: @Contextual Color = Color(255, 0, 0),
        val crystalOverlay: CrystalOverlay = CrystalOverlay()
    ) {
        @Serializable
        data class HollowsWaypoints(
            val goblinKing: Boolean = true,
            val goblinQueen: Boolean = true,
            val precursorCity: Boolean = true,
            val jungleTemple: Boolean = true,
            val amethystCrystal: Boolean = true,
            val odawa: Boolean = true,
            val khazadDum: Boolean = true,
            val minesOfDivan: Boolean = true,
            val nucleus: Boolean = true,
            val fairyGrotto: Boolean = true,
            val corleone: Boolean = true,
            val keyGuardian: Boolean = true
        )

        @Serializable
        data class CrystalOverlay(val enabled: Boolean = true, var x: Int = 5, var y: Int = 200)
    }
}