@file:Suppress("USELESS_CAST")

package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.dsl.*
import dev.nyon.skylper.config.config
import java.awt.Color

fun RootDsl.appendMiningCategory() {
    val mining by categories.registering {
        val cooldownIndicator by rootOptions.registering {
            binding(true, { config.mining.miningAbilityIndicator }, { config.mining.miningAbilityIndicator = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val cooldownNotification by rootOptions.registering {
            binding(true, { config.mining.miningAbilityNotification }, { config.mining.miningAbilityNotification = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val cooldownNotificationOnMiningIslands by rootOptions.registering {
            binding(
                true,
                { config.mining.miningAbilityNotificationOnMiningIslands },
                { config.mining.miningAbilityNotificationOnMiningIslands = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val totalPowderOverlay by groups.registering {
            descriptionBuilder {
                addDefaultText(1)
            }

            val enabled by options.registering {
                binding(
                    true,
                    { config.mining.totalPowderOverlay.enabled },
                    { config.mining.totalPowderOverlay.enabled = it })
                controller = tickBox()
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val x by options.registering {
                binding(5, { config.mining.totalPowderOverlay.x }, { config.mining.totalPowderOverlay.x = it })
                controller = numberField(0 as Int)
                descriptionBuilder {
                    addDefaultText(1)
                }
            }

            val y by options.registering {
                binding(300, { config.mining.totalPowderOverlay.y }, { config.mining.totalPowderOverlay.y = it })
                controller = numberField(0 as Int)
                descriptionBuilder {
                    addDefaultText(1)
                }
            }
        }

        val highlightCompletedCommissions by rootOptions.registering {
            binding(
                true,
                { config.mining.highlightCompletedCommissions },
                { config.mining.highlightCompletedCommissions = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }

        val completedCommissionsHighlightColor by rootOptions.registering {
            binding(
                Color(255, 0, 0, 50),
                { config.mining.completedCommissionsHighlightColor },
                { config.mining.completedCommissionsHighlightColor = it })
            controller = colorPicker()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
    }
}