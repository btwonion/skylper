package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.doubleOrNull
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.extensions.singleGroup
import dev.nyon.skylper.skyblock.models.mining.PowderType
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.ChestReward
import dev.nyon.skylper.skyblock.tracker.mining.crystalHollows.powder.PowderGrindingTracker.data
import dev.nyon.skylper.skyblock.tracker.mining.crystalHollows.powder.PowderGrindingTracker.startTime
import kotlinx.datetime.Clock

object CrystalHollowsPowderGrindingApi {
    private val powderStartedPattern get() = regex("chat.hollows.tracker.powder.started")
    private val powderEndedPattern get() = regex("chat.hollows.tracker.powder.ended")
    private val pickedLockPattern get() = regex("chat.hollows.tracker.picked")
    private val powderBossBarPattern get() = regex("bossbar.hollows.powder")

    var doublePowderActive: Boolean = false

    private val chestRewardToPowderType = mapOf(
        ChestReward.MITHRIL_POWDER to PowderType.MITHRIL, ChestReward.GEMSTONE_POWDER to PowderType.GEMSTONE
    )

    @Suppress("unused")
    private val chatListener = listenInfoEvent<MessageEvent> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent
        val now = Clock.System.now()
        when {
            powderStartedPattern.matches(rawText) -> doublePowderActive = true
            powderEndedPattern.matches(rawText) -> doublePowderActive = false
            pickedLockPattern.matches(rawText) -> EventHandler.invokeEvent(TreasureChestPickEvent)
        }

        val rewards = ChestReward.entries.associateWith { reward ->
            val regex = reward.getRegex()
            if (!regex.matches(rawText)) return@associateWith 0
            if (startTime == null) startTime = now
            val amount = regex.singleGroup(rawText)?.doubleOrNull()?.toInt() ?: return@associateWith 0
            amount
        }.toMutableMap().filter { it.value != 0 }

        EventHandler.invokeEvent(TreasureChestRewardsEvent(rewards))

        rewards.forEach { (reward, amount) ->
            if (!chestRewardToPowderType.containsKey(reward)) return@forEach
            val type = chestRewardToPowderType[reward] ?: return@forEach
            val fixedAmount = amount * PowderApi.getPowderMultiplier(type)
            EventHandler.invokeEvent(PowderGainEvent(type, fixedAmount.toInt()))
        }
    }

    @Suppress("unused")
    private val bossBarListener = listenInfoEvent<BossBarNameUpdate> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent
        if (!powderBossBarPattern.matches(rawText)) return@listenInfoEvent
        data.doublePowderActive = true
    }
}