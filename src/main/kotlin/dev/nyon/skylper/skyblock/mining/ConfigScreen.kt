package dev.nyon.skylper.skyblock.mining

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.*

fun YetAnotherConfigLib.Builder.appendMiningCategory() = category("mining") {
    val categoryKey = "mining"

    val cooldownIndicatorKey = "cooldown_indicator"
    primitive(categoryKey, cooldownIndicatorKey) {
        description(categoryKey, cooldownIndicatorKey)
        getSet({ config.mining.miningAbilityIndicator }, { config.mining.miningAbilityIndicator = it })
        tickBox()
    }

    val cooldownNotificationKey = "cooldown_notification"
    primitive(categoryKey, cooldownNotificationKey) {
        description(categoryKey, cooldownNotificationKey)
        getSet({ config.mining.availableAbilityNotification }, { config.mining.availableAbilityNotification = it })
        tickBox()
    }

    val cooldownNotificationMiningIslandKey = "cooldown_notification_only_mining_islands"
    primitive(categoryKey, cooldownNotificationMiningIslandKey) {
        description(categoryKey, cooldownNotificationMiningIslandKey)
        getSet({ config.mining.availableAbilityNotificationOnMiningIslands },
            { config.mining.availableAbilityNotificationOnMiningIslands = it })
        tickBox()
    }

    val totalPowderOverlayKey = "total_powder"
    overlayConfig(categoryKey,
        totalPowderOverlayKey,
        { config.mining.totalPowderOverlay.enabled },
        { config.mining.totalPowderOverlay.enabled = it },
        { config.mining.totalPowderOverlay.x },
        { config.mining.totalPowderOverlay.x = it },
        { config.mining.totalPowderOverlay.y },
        { config.mining.totalPowderOverlay.y = it })
}