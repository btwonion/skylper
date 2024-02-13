package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.*
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.render.SkylperHudModifier
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

@Suppress("SpellCheckingInspection")
internal fun createYaclScreen(parent: Screen?): Screen {
    val builder = YetAnotherConfigLib.createBuilder()

    builder.title(Component.translatable("menu.skylper.config.name"))
    builder.appendGeneralCategory()
    builder.appendCrystalHollowsCategory()

    builder.save { saveConfig(config) }
    val screen = builder.build()
    return screen.generateScreen(parent)
}

private fun YetAnotherConfigLib.Builder.appendGeneralCategory() = category("general") {
    val categoryKey = "general"

    val hudKey = "hud"
    action(categoryKey, hudKey) {
        description(categoryKey, hudKey)
        action { screen, _ ->
            minecraft.setScreen(SkylperHudModifier(screen))
        }
    }
}

private fun YetAnotherConfigLib.Builder.appendCrystalHollowsCategory() = category("hollows") {
    val categoryKey = "hollows"

    val highlightChestsKey = "chest_highlight"
    primitive(categoryKey, highlightChestsKey) {
        description(categoryKey, highlightChestsKey)
        getSet({ config.crystalHollows.highlightChests }, { config.crystalHollows.highlightChests = it })
        tickBox()
    }

    val chestHighlightColor = "$highlightChestsKey.color"
    primitive(categoryKey, chestHighlightColor) {
        description(categoryKey, chestHighlightColor)
        getSet({ config.crystalHollows.chestHighlightColor }, { config.crystalHollows.chestHighlightColor = it })
        field(true)
    }

    val locationGroupKey = "locations"
    subGroup(categoryKey, locationGroupKey) {
        description(categoryKey, locationGroupKey)
        val parseLocationKey = "$locationGroupKey.parse_location"
        primitive(categoryKey, parseLocationKey) {
            description(categoryKey, parseLocationKey)
            getSet({ config.crystalHollows.parseLocationChats }, { config.crystalHollows.parseLocationChats = it })
            tickBox()
        }

        val autoAddLocationKey = "$locationGroupKey.auto_add_location"
        primitive(categoryKey, autoAddLocationKey) {
            description(categoryKey, autoAddLocationKey)
            getSet({ config.crystalHollows.automaticallyAddLocations },
                { config.crystalHollows.automaticallyAddLocations = it })
            tickBox()
        }
    }

    val waypointsGroupKey = "waypoints"
    subGroup(categoryKey, waypointsGroupKey) {
        description(categoryKey, waypointsGroupKey)
        val generalWaypointKey = "$waypointsGroupKey.waypoint"
        primitive(categoryKey, "$generalWaypointKey.goblin_king") {
            description(categoryKey, generalWaypointKey, "King Yolkar")
            getSet({ config.crystalHollows.hollowsWaypoints.goblinKing },
                { config.crystalHollows.hollowsWaypoints.goblinKing = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.goblin_queen") {
            description(categoryKey, generalWaypointKey, "Goblin Queen")
            getSet({ config.crystalHollows.hollowsWaypoints.goblinQueen },
                { config.crystalHollows.hollowsWaypoints.goblinQueen = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.precursor_city") {
            description(categoryKey, generalWaypointKey, "Precursor City")
            getSet({ config.crystalHollows.hollowsWaypoints.precursorCity },
                { config.crystalHollows.hollowsWaypoints.precursorCity = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.jungle_temple") {
            description(categoryKey, generalWaypointKey, "Jungle Temple")
            getSet({ config.crystalHollows.hollowsWaypoints.jungleTemple },
                { config.crystalHollows.hollowsWaypoints.jungleTemple = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.amethyst_crystal") {
            description(categoryKey, generalWaypointKey, "Amethyst Crystal")
            getSet({ config.crystalHollows.hollowsWaypoints.amethystCrystal },
                { config.crystalHollows.hollowsWaypoints.amethystCrystal = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.odawa") {
            description(categoryKey, generalWaypointKey, "Odawa")
            getSet({ config.crystalHollows.hollowsWaypoints.odawa },
                { config.crystalHollows.hollowsWaypoints.odawa = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.khazad_dum") {
            description(categoryKey, generalWaypointKey, "Khazad-dûm")
            getSet({ config.crystalHollows.hollowsWaypoints.khazadDum },
                { config.crystalHollows.hollowsWaypoints.khazadDum = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.divan_mines") {
            description(categoryKey, generalWaypointKey, "Mines of Divan")
            getSet({ config.crystalHollows.hollowsWaypoints.minesOfDivan },
                { config.crystalHollows.hollowsWaypoints.minesOfDivan = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.nucleus") {
            description(categoryKey, generalWaypointKey, "Crystal Nucleus")
            getSet({ config.crystalHollows.hollowsWaypoints.nucleus },
                { config.crystalHollows.hollowsWaypoints.nucleus = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.fairy_grotto") {
            description(categoryKey, generalWaypointKey, "Fairy Grotto")
            getSet({ config.crystalHollows.hollowsWaypoints.fairyGrotto },
                { config.crystalHollows.hollowsWaypoints.fairyGrotto = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.corleone") {
            description(categoryKey, generalWaypointKey, "Boss Corleone")
            getSet({ config.crystalHollows.hollowsWaypoints.corleone },
                { config.crystalHollows.hollowsWaypoints.corleone = it })
            tickBox()
        }

        primitive(categoryKey, "$generalWaypointKey.key_guardian") {
            description(categoryKey, generalWaypointKey, "Key Guardian")
            getSet({ config.crystalHollows.hollowsWaypoints.keyGuardian },
                { config.crystalHollows.hollowsWaypoints.keyGuardian = it })
            tickBox()
        }
    }

    val overlayGroupKey = "crystal_overlay"
    overlayConfig(categoryKey,
        overlayGroupKey,
        { config.crystalHollows.crystalOverlay.enabled },
        { config.crystalHollows.crystalOverlay.enabled = it },
        { config.crystalHollows.crystalOverlay.x },
        { config.crystalHollows.crystalOverlay.x = it },
        { config.crystalHollows.crystalOverlay.y },
        { config.crystalHollows.crystalOverlay.y = it })
}