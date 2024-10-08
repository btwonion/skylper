package dev.nyon.skylper.config

import dev.nyon.skylper.extensions.ColorSerializer
import dev.nyon.skylper.extensions.RegexSerializer
import dev.nyon.skylper.extensions.Vec3Serializer
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
        contextual(RegexSerializer)
        contextual(Vec3Serializer)
    }
}
val configDir: Path = FabricLoader.getInstance().configDir.resolve("skylper")
var config: Config = Config()

@Serializable
data class Config(val mining: MiningConfig = MiningConfig(), val menu: Menu = Menu(), val misc: Misc = Misc()) {
    @Serializable
    data class MiningConfig(
        val crystalHollows: CrystalHollowsConfig = CrystalHollowsConfig(),
        var miningAbilityNotification: Boolean = true,
        var miningAbilityNotificationOnMiningIslands: Boolean = true,
        var miningAbilityIndicator: Boolean = true,
        val totalPowderOverlay: TotalPowderOverlay = TotalPowderOverlay(),
        val eventOverlay: EventOverlay = EventOverlay(),
        var highlightCompletedCommissions: Boolean = true,
        var completedCommissionsHighlightColor: @Contextual Color = Color(255, 0, 0, 50)
    )

    @Serializable
    data class CrystalHollowsConfig(
        val hollowsWaypoints: HollowsWaypoints = HollowsWaypoints(),
        var parseLocationChats: Boolean = true,
        var automaticallyAddLocations: Boolean = true,
        var highlightChests: Boolean = true,
        var chestHighlightColor: @Contextual Color = Color(255, 0, 0, 100),
        val crystalOverlay: CrystalOverlay = CrystalOverlay(),
        val powderGrindingOverlay: GrindingOverlay = GrindingOverlay(),
        var autoRenewPass: Boolean = true,
        var chestLockHighlight: Boolean = true
    ) {
        @Serializable
        data class HollowsWaypoints(
            var goblinKing: Boolean = true,
            var goblinQueen: Boolean = true,
            var precursorCity: Boolean = true,
            var jungleTemple: Boolean = true,
            var amethystCrystal: Boolean = false,
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

        @Serializable
        data class GrindingOverlay(
            var enabled: Boolean = true,
            var x: Int = 5,
            var y: Int = 0,
            var chests: ResourceConfig = ResourceConfig(),
            var gemstone: ResourceConfig = ResourceConfig(),
            var mithril: ResourceConfig = ResourceConfig(),
            var glacite: ResourceConfig = ResourceConfig(total = false, perHour = false),
            var doublePowder: Boolean = true,
            var sessionTime: Boolean = true
        ) {
            @Serializable
            data class ResourceConfig(
                var total: Boolean = true, var perMinute: Boolean = false, var perHour: Boolean = true
            )
        }
    }

    @Serializable
    data class TotalPowderOverlay(var enabled: Boolean = true, var x: Int = 5, var y: Int = 300)

    @Serializable
    data class EventOverlay(var enabled: Boolean = true, var x: Int = 5, var y: Int = 500)

    @Serializable
    data class Menu(val collections: Collections = Collections(), val bestiary: Bestiary = Bestiary()) {
        @Serializable
        data class Collections(
            var highlightNonCompletedCollections: Boolean = true,
            var nonCompletedCollectionHighlightColor: @Contextual Color = Color(255, 0, 0, 255)
        )

        @Serializable
        data class Bestiary(
            var highlightNonCompletedBestiary: Boolean = true,
            var nonCompletedBestiaryHighlightColor: @Contextual Color = Color(255, 0, 0, 255)
        )
    }

    @Serializable
    data class Misc(var recognizeLobbies: Boolean = true)
}
