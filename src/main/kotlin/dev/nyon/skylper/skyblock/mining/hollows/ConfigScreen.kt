package dev.nyon.skylper.skyblock.mining.hollows

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.*
import dev.nyon.skylper.skyblock.mining.hollows.tracker.powder.PowderGrindingTracker

fun YetAnotherConfigLib.Builder.appendCrystalHollowsCategory() =
    category("hollows") {
        val categoryKey = "hollows"

        // Chest highlight toggle
        val highlightChestsKey = "chest_highlight"
        primitive(categoryKey, highlightChestsKey) {
            description(categoryKey, highlightChestsKey)
            getSet({ config.mining.crystalHollows.highlightChests }, { config.mining.crystalHollows.highlightChests = it })
            tickBox()
        }

        // Chest highlight color
        val chestHighlightColor = "$highlightChestsKey.color"
        primitive(categoryKey, chestHighlightColor) {
            description(categoryKey, chestHighlightColor)
            getSet({ config.mining.crystalHollows.chestHighlightColor }, { config.mining.crystalHollows.chestHighlightColor = it })
            field(true)
        }

        // Chest lock highlight
        val chestLockHighlight = "chest_lock_highlight"
        primitive(categoryKey, chestLockHighlight) {
            description(categoryKey, chestLockHighlight)
            getSet({ config.mining.crystalHollows.chestLockHighlight }, { config.mining.crystalHollows.chestLockHighlight = it })
            tickBox()
        }

        // Auto Pass Renew toggle
        val autoPassRenewKey = "auto_pass_renew"
        primitive(categoryKey, autoPassRenewKey) {
            description(categoryKey, autoPassRenewKey)
            getSet({ config.mining.crystalHollows.autoRenewPass }, { config.mining.crystalHollows.autoRenewPass = it })
            tickBox()
        }

        // Metal detector solver toggle
        val metalDetectorKey = "metal_detector"
        primitive(categoryKey, metalDetectorKey) {
            description(categoryKey, metalDetectorKey)
            getSet({ config.mining.crystalHollows.metalDetectorHelper }, { config.mining.crystalHollows.metalDetectorHelper = it })
            tickBox()
        }

        // Chat locations toggles
        val locationGroupKey = "locations"
        subGroup(categoryKey, locationGroupKey) {
            description(categoryKey, locationGroupKey) // parse locations toggle
            val parseLocationKey = "$locationGroupKey.parse_location"
            primitive(categoryKey, parseLocationKey) {
                description(categoryKey, parseLocationKey)
                getSet({ config.mining.crystalHollows.parseLocationChats }, { config.mining.crystalHollows.parseLocationChats = it })
                tickBox()
            }

            // auto add parsed locations toggle
            val autoAddLocationKey = "$locationGroupKey.auto_add_location"
            primitive(categoryKey, autoAddLocationKey) {
                description(categoryKey, autoAddLocationKey)
                getSet(
                    { config.mining.crystalHollows.automaticallyAddLocations },
                    { config.mining.crystalHollows.automaticallyAddLocations = it }
                )
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
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.goblinKing },
                    { config.mining.crystalHollows.hollowsWaypoints.goblinKing = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.goblin_queen") {
                description(categoryKey, generalWaypointKey, "Goblin Queen")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.goblinQueen },
                    { config.mining.crystalHollows.hollowsWaypoints.goblinQueen = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.precursor_city") {
                description(categoryKey, generalWaypointKey, "Precursor City")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.precursorCity },
                    { config.mining.crystalHollows.hollowsWaypoints.precursorCity = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.jungle_temple") {
                description(categoryKey, generalWaypointKey, "Jungle Temple")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.jungleTemple },
                    { config.mining.crystalHollows.hollowsWaypoints.jungleTemple = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.odawa") {
                description(categoryKey, generalWaypointKey, "Odawa")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.odawa },
                    { config.mining.crystalHollows.hollowsWaypoints.odawa = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.khazad_dum") {
                description(categoryKey, generalWaypointKey, "Khazad-dûm")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.khazadDum },
                    { config.mining.crystalHollows.hollowsWaypoints.khazadDum = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.divan_mines") {
                description(categoryKey, generalWaypointKey, "Mines of Divan")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.minesOfDivan },
                    { config.mining.crystalHollows.hollowsWaypoints.minesOfDivan = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.nucleus") {
                description(categoryKey, generalWaypointKey, "Crystal Nucleus")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.nucleus },
                    { config.mining.crystalHollows.hollowsWaypoints.nucleus = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.fairy_grotto") {
                description(categoryKey, generalWaypointKey, "Fairy Grotto")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.fairyGrotto },
                    { config.mining.crystalHollows.hollowsWaypoints.fairyGrotto = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.corleone") {
                description(categoryKey, generalWaypointKey, "Boss Corleone")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.corleone },
                    { config.mining.crystalHollows.hollowsWaypoints.corleone = it }
                )
                tickBox()
            }

            primitive(categoryKey, "$generalWaypointKey.key_guardian") {
                description(categoryKey, generalWaypointKey, "Key Guardian")
                getSet(
                    { config.mining.crystalHollows.hollowsWaypoints.keyGuardian },
                    { config.mining.crystalHollows.hollowsWaypoints.keyGuardian = it }
                )
                tickBox()
            }
        }

        // crystal overlay
        val overlayGroupKey = "crystal_overlay"
        overlayConfig(
            categoryKey,
            overlayGroupKey,
            { config.mining.crystalHollows.crystalOverlay.enabled },
            { config.mining.crystalHollows.crystalOverlay.enabled = it },
            { config.mining.crystalHollows.crystalOverlay.x },
            { config.mining.crystalHollows.crystalOverlay.x = it },
            { config.mining.crystalHollows.crystalOverlay.y },
            { config.mining.crystalHollows.crystalOverlay.y = it }
        )

        // tracker overlay
        trackerConfig(
            PowderGrindingTracker,
            { config.mining.crystalHollows.powderGrindingOverlay.enabled },
            { config.mining.crystalHollows.powderGrindingOverlay.enabled = it },
            { config.mining.crystalHollows.powderGrindingOverlay.x },
            { config.mining.crystalHollows.powderGrindingOverlay.x = it },
            { config.mining.crystalHollows.powderGrindingOverlay.y },
            { config.mining.crystalHollows.powderGrindingOverlay.y = it }
        )
    }
