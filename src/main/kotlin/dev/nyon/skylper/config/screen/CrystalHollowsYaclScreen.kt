package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.dsl.*
import dev.nyon.skylper.config.config
import dev.nyon.skylper.minecraft
import java.awt.Color

fun RootDsl.appendHollowsCategory() {
    val hollows by categories.registering {
        val highlightChest by rootOptions.registering {
            binding(
                true,
                { config.mining.crystalHollows.highlightChests },
                { config.mining.crystalHollows.highlightChests = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val highlightChestColor by rootOptions.registering {
            binding(
                Color(255, 0, 0, 100),
                { config.mining.crystalHollows.chestHighlightColor },
                { config.mining.crystalHollows.chestHighlightColor = it })
            controller = colorPicker()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val chestLockHighlight by rootOptions.registering {
            binding(
                true,
                { config.mining.crystalHollows.chestLockHighlight },
                { config.mining.crystalHollows.chestLockHighlight = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val autoPassRenew by rootOptions.registering {
            binding(
                true,
                { config.mining.crystalHollows.autoRenewPass },
                { config.mining.crystalHollows.autoRenewPass = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val locations by groups.registering {
            descriptionBuilder {
                addDefaultText(1)
            }

            val parseLocations by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.parseLocationChats },
                    { config.mining.crystalHollows.parseLocationChats = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val autoAddLocations by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.automaticallyAddLocations },
                    { config.mining.crystalHollows.automaticallyAddLocations = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }
        }

        val locationsScreen by rootOptions.registeringButton {
            action { parent, _ ->
                minecraft.setScreen(createLocationScreen(parent))
            }

            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val waypoints by groups.registering {
            descriptionBuilder {
                addDefaultText(1)
            }

            val goblinKing by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.goblinKing },
                    { config.mining.crystalHollows.hollowsWaypoints.goblinKing = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val goblinQueen by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.goblinQueen },
                    { config.mining.crystalHollows.hollowsWaypoints.goblinQueen = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val precursorCity by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.precursorCity },
                    { config.mining.crystalHollows.hollowsWaypoints.precursorCity = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val jungleTemple by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.jungleTemple },
                    { config.mining.crystalHollows.hollowsWaypoints.jungleTemple = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val amethystCrystal by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.amethystCrystal },
                    { config.mining.crystalHollows.hollowsWaypoints.amethystCrystal = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val odawa by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.odawa },
                    { config.mining.crystalHollows.hollowsWaypoints.odawa = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val khazadDum by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.khazadDum },
                    { config.mining.crystalHollows.hollowsWaypoints.khazadDum = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val divanMines by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.minesOfDivan },
                    { config.mining.crystalHollows.hollowsWaypoints.minesOfDivan = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val nucleus by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.nucleus },
                    { config.mining.crystalHollows.hollowsWaypoints.nucleus = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val fairyGrotto by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.fairyGrotto },
                    { config.mining.crystalHollows.hollowsWaypoints.fairyGrotto = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val corleone by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.corleone },
                    { config.mining.crystalHollows.hollowsWaypoints.corleone = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val keyGuardian by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.hollowsWaypoints.keyGuardian },
                    { config.mining.crystalHollows.hollowsWaypoints.keyGuardian = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }
        }

        val crystalOverlay by groups.registering {
            descriptionBuilder {
                addDefaultText(1)
            }

            val enabled by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.crystalOverlay.enabled },
                    { config.mining.crystalHollows.crystalOverlay.enabled = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val x by options.registering {
                binding(
                    5,
                    { config.mining.crystalHollows.crystalOverlay.x },
                    { config.mining.crystalHollows.crystalOverlay.x = it })
                controller = numberField(0 as Int)
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val y by options.registering {
                binding(
                    200,
                    { config.mining.crystalHollows.crystalOverlay.y },
                    { config.mining.crystalHollows.crystalOverlay.y = it })
                controller = numberField(0 as Int)
                descriptionBuilder {
                    addDefaultText(1)
                }
            }
        }

        val powderGrindingOverlay by groups.registering {
            descriptionBuilder {
                addDefaultText(1)
            }

            val enabled by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.enabled },
                    { config.mining.crystalHollows.powderGrindingOverlay.enabled = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val x by options.registering {
                binding(
                    5,
                    { config.mining.crystalHollows.powderGrindingOverlay.x },
                    { config.mining.crystalHollows.powderGrindingOverlay.x = it })
                controller = numberField(0 as Int)
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val y by options.registering {
                binding(
                    200,
                    { config.mining.crystalHollows.powderGrindingOverlay.y },
                    { config.mining.crystalHollows.powderGrindingOverlay.y = it })
                controller = numberField(0 as Int)
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val chestTotal by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.chests.total },
                    { config.mining.crystalHollows.powderGrindingOverlay.chests.total = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val chestMinute by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.chests.perMinute },
                    { config.mining.crystalHollows.powderGrindingOverlay.chests.perMinute = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val chestHour by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.chests.perHour },
                    { config.mining.crystalHollows.powderGrindingOverlay.chests.perHour = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val gemstoneTotal by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.gemstone.total },
                    { config.mining.crystalHollows.powderGrindingOverlay.gemstone.total = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val gemstoneMinute by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.gemstone.perMinute },
                    { config.mining.crystalHollows.powderGrindingOverlay.gemstone.perMinute = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val gemstoneHour by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.gemstone.perHour },
                    { config.mining.crystalHollows.powderGrindingOverlay.gemstone.perHour = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val mithrilTotal by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.mithril.total },
                    { config.mining.crystalHollows.powderGrindingOverlay.mithril.total = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val mithrilMinute by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.mithril.perMinute },
                    { config.mining.crystalHollows.powderGrindingOverlay.mithril.perMinute = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val mithrilHour by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.mithril.perHour },
                    { config.mining.crystalHollows.powderGrindingOverlay.mithril.perHour = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val doublePowder by options.registering {
                binding(
                    true,
                    { config.mining.crystalHollows.powderGrindingOverlay.doublePowder },
                    { config.mining.crystalHollows.powderGrindingOverlay.doublePowder = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }
        }
    }
}