package dev.nyon.skylper.skyblock.cooldowns

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.SkyblockEnterEvent
import dev.nyon.skylper.extensions.internalName
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.online.Cooldowns
import dev.nyon.skylper.skyblock.data.online.IslandGroups
import dev.nyon.skylper.skyblock.data.online.ToolGroups
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object MiningCooldown : Cooldown {
    override var cooldownEnd: Instant? = null

    private val abilityUsedRegex get() = regex("chat.mining.abilityUsed")
    private val abilityAvailable get() = regex("chat.mining.abilityAvailable")

    override fun isEnabled(): Boolean {
        return config.mining.miningAbilityIndicator
    }

    override fun isCorrectItem(stack: ItemStack): Boolean {
        return ToolGroups.groups.mining.contains(stack.internalName)
    }

    @Suppress("unused")
    private val skyblockJoin = listenInfoEvent<SkyblockEnterEvent> {
        val now = Clock.System.now()
        mcScope.launch {
            delay(3.seconds)
            val cooldown = getCooldownTime() ?: return@launch
            cooldownEnd = now + (cooldown / 2)
        }
    }

    @Suppress("unused")
    private val listenChat = listenInfoEvent<MessageEvent> {
        if (abilityUsedRegex.matches(rawText)) {
            abilityUsed()
            return@listenInfoEvent
        }

        if (abilityAvailable.matches(rawText)) {
            abilityAvailable()
            return@listenInfoEvent
        }
    }

    private fun abilityAvailable() {
        cooldownEnd = null
        if (config.mining.miningAbilityNotification) {
            if (config.mining.miningAbilityNotificationOnMiningIslands && !IslandGroups.groups.mining.contains(
                    PlayerSessionData.currentArea
                )
            ) {
                return
            }
            minecraft.gui.setTitle(Component.literal("T")
                .withStyle { it.withObfuscated(true).withColor(ChatFormatting.WHITE) }
                .append(Component.literal(" Pickaxe Ability available ")
                    .withStyle { it.withObfuscated(false).withColor(ChatFormatting.AQUA).withBold(true) })
                .append(Component.literal("T").withStyle { it.withObfuscated(true).withColor(ChatFormatting.WHITE) })
            )
        }
    }

    private fun abilityUsed() {
        val now = Clock.System.now()
        val cooldownTime = getCooldownTime()
        if (cooldownTime == null) {
            noAbilitySelectedNotice()
            return
        }
        cooldownEnd = now + cooldownTime
    }

    private fun noAbilitySelectedNotice() {
        minecraft.player?.sendSystemMessage(Component.translatable("chat.skylper.hollows.pickaxe_cooldown.not_found")
            .withStyle { it.withColor(ChatFormatting.RED) })
    }

    override fun getCooldownTime(): Duration? {
        val abilityLevel = if (currentProfile.heartOfTheMountain.peakOfTheMountainLevel == 0) 1 else 2
        val cooldowns =
            Cooldowns.cooldowns.mining[currentProfile.heartOfTheMountain.pickaxeAbility ?: return null] ?: return null
        return cooldowns[abilityLevel].seconds
    }
}

interface AbilityCooldownIdentifier {
    @Suppress("FunctionName")
    fun `skylper$getCooldownPercent`(
        itemStack: ItemStack, partialTicks: Float
    ): Float
}

