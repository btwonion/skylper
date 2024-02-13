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
        var parseLocationChats: Boolean = true,
        var automaticallyAddLocations: Boolean = true,
        var highlightChests: Boolean = true,
        var chestHighlightColor: @Contextual Color = Color(255, 0, 0, 100),
        val crystalOverlay: CrystalOverlay = CrystalOverlay()
    ) {
        @Serializable
        data class HollowsWaypoints(
            var goblinKing: Boolean = true,
            var goblinQueen: Boolean = true,
            var precursorCity: Boolean = true,
            var jungleTemple: Boolean = true,
            var amethystCrystal: Boolean = true,
            var odawa: Boolean = true,
            var khazadDum: Boolean = true,
            var minesOfDivan: Boolean = true,
            var nucleus: Boolean = true,
            var fairyGrotto: Boolean = true,
            var corleone: Boolean = true,
            var keyGuardian: Boolean = true
        )

        @Serializable
        data class CrystalOverlay(var enabled: Boolean = true, var x: Int = 5, var y: Int = 200)
    }
}