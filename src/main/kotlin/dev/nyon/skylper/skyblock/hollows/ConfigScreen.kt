package dev.nyon.skylper.skyblock.hollows

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.*

fun YetAnotherConfigLib.Builder.appendCrystalHollowsCategory() = category("hollows") {
    val categoryKey = "hollows"

    // Chest highlight toggle
    val highlightChestsKey = "chest_highlight"
    primitive(categoryKey, highlightChestsKey) {
        description(categoryKey, highlightChestsKey)
        getSet({ config.crystalHollows.highlightChests }, { config.crystalHollows.highlightChests = it })
        tickBox()
    }

    // Chest highlight color
    val chestHighlightColor = "$highlightChestsKey.color"
    primitive(categoryKey, chestHighlightColor) {
        description(categoryKey, chestHighlightColor)
        getSet({ config.crystalHollows.chestHighlightColor }, { config.crystalHollows.chestHighlightColor = it })
        field(true)
    }

    // Auto Pass Renew toggle
    val autoPassRenewKey = "auto_pass_renew"
    primitive(categoryKey, autoPassRenewKey) {
        description(categoryKey, autoPassRenewKey)
        getSet({ config.crystalHollows.autoRenewPass }, { config.crystalHollows.autoRenewPass = it })
        tickBox()
    }

    // Metal detector solver toggle
    val metalDetectorKey = "metal_detector"
    primitive(categoryKey, metalDetectorKey) {
        description(categoryKey, metalDetectorKey)
        getSet({ config.crystalHollows.metalDetectorHelper }, { config.crystalHollows.metalDetectorHelper = it })
        tickBox()
    }

    // Chat locations toggles
    val locationGroupKey = "locations"
    subGroup(categoryKey, locationGroupKey) {
        description(categoryKey, locationGroupKey)
        // parse locations toggle
        val parseLocationKey = "$locationGroupKey.parse_location"
        primitive(categoryKey, parseLocationKey) {
            description(categoryKey, parseLocationKey)
            getSet({ config.crystalHollows.parseLocationChats }, { config.crystalHollows.parseLocationChats = it })
            tickBox()
        }

        // auto add parsed locations toggle
        val autoAddLocationKey = "$locationGroupKey.auto_add_location"
        primitive(categoryKey, autoAddLocationKey) {
            description(categoryKey, autoAddLocationKey)
            getSet({ config.crystalHollows.automaticallyAddLocations },
                { config.crystalHollows.automaticallyAddLocations = it })
            tickBox()
        }
    }

    // structure waypoint toggles
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

    // crystal overlay
    val overlayGroupKey = "crystal_overlay"
    overlayConfig(categoryKey,
        overlayGroupKey,
        { config.crystalHollows.crystalOverlay.enabled },
        { config.crystalHollows.crystalOverlay.enabled = it },
        { config.crystalHollows.crystalOverlay.x },
        { config.crystalHollows.crystalOverlay.x = it },
        { config.crystalHollows.crystalOverlay.y },
        { config.crystalHollows.crystalOverlay.y = it })

    // tracker overlay
    val trackerOverlayKey = "tracker_overlay"
    overlayConfig(categoryKey,
        trackerOverlayKey,
        { config.crystalHollows.powderGrindingOverlay.enabled },
        { config.crystalHollows.powderGrindingOverlay.enabled = it },
        { config.crystalHollows.powderGrindingOverlay.x },
        { config.crystalHollows.powderGrindingOverlay.x = it },
        { config.crystalHollows.powderGrindingOverlay.y },
        { config.crystalHollows.powderGrindingOverlay.y = it }) {

        // opened chests toggle
        val openedChestsKey = "$trackerOverlayKey.opened_chests"
        primitive(categoryKey, openedChestsKey) {
            description(categoryKey, openedChestsKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.openedChests },
                { config.crystalHollows.powderGrindingOverlay.openedChests = it })
            tickBox()
        }

        // Powder setting toggles
        val farmedGemstoneKey = "$trackerOverlayKey.farmed_gemstone"
        primitive(categoryKey, farmedGemstoneKey) {
            description(categoryKey, farmedGemstoneKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.farmedGemstonePowder },
                { config.crystalHollows.powderGrindingOverlay.farmedGemstonePowder = it })
            tickBox()
        }

        val farmedGemstoneMinuteKey = "$trackerOverlayKey.farmed_gemstone_minute"
        primitive(categoryKey, farmedGemstoneMinuteKey) {
            description(categoryKey, farmedGemstoneMinuteKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.gemstonePowderPerMinute },
                { config.crystalHollows.powderGrindingOverlay.gemstonePowderPerMinute = it })
            tickBox()
        }

        val farmedGemstoneHourKey = "$trackerOverlayKey.farmed_gemstone_hour"
        primitive(categoryKey, farmedGemstoneHourKey) {
            description(categoryKey, farmedGemstoneHourKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.gemstonePowderPerHour },
                { config.crystalHollows.powderGrindingOverlay.gemstonePowderPerHour = it })
            tickBox()
        }

        val farmedMithrilKey = "$trackerOverlayKey.farmed_mithril"
        primitive(categoryKey, farmedMithrilKey) {
            description(categoryKey, farmedMithrilKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.farmedMithrilPowder },
                { config.crystalHollows.powderGrindingOverlay.farmedMithrilPowder = it })
            tickBox()
        }

        val farmedMithrilMinuteKey = "$trackerOverlayKey.farmed_mithril_minute"
        primitive(categoryKey, farmedMithrilMinuteKey) {
            description(categoryKey, farmedMithrilMinuteKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.mithrilPowderPerMinute },
                { config.crystalHollows.powderGrindingOverlay.mithrilPowderPerMinute = it })
            tickBox()
        }

        val farmedMithrilHourKey = "$trackerOverlayKey.farmed_mithril_hour"
        primitive(categoryKey, farmedMithrilHourKey) {
            description(categoryKey, farmedMithrilHourKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.mithrilPowderPerHour },
                { config.crystalHollows.powderGrindingOverlay.mithrilPowderPerHour = it })
            tickBox()
        }

        // double powder toggle
        val doublePowderKey = "$trackerOverlayKey.double_powder"
        primitive(categoryKey, doublePowderKey) {
            description(categoryKey, doublePowderKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.doublePowder },
                { config.crystalHollows.powderGrindingOverlay.doublePowder = it })
            tickBox()
        }

        // session time toggle
        val sessionTimeKey = "$trackerOverlayKey.session_time"
        primitive(categoryKey, sessionTimeKey) {
            description(categoryKey, sessionTimeKey)
            getSet({ config.crystalHollows.powderGrindingOverlay.sessionTime },
                { config.crystalHollows.powderGrindingOverlay.sessionTime = it })
            tickBox()
        }
    }
}