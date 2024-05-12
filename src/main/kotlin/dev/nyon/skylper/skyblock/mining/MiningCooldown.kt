package dev.nyon.skylper.skyblock.mining

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.extensions.SkyblockEnterEvent
import dev.nyon.skylper.extensions.internalName
import dev.nyon.skylper.extensions.render.cooldown.Cooldown
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.Mining
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
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

    override fun isEnabled(): Boolean {
        return config.mining.miningAbilityIndicator
    }

    private val internalMiningItemNames = setOf(
        "WOOD_PICKAXE",
        "GOLD_PICKAXE",
        "STONE_PICKAXE",
        "IRON_PICKAXE",
        "DIAMOND_PICKAXE",
        "ZOMBIE_PICKAXE",
        "ROOKIE_PICKAXE",
        "PROMISING_PICKAXE",
        "STONK_PICKAXE",
        "JUNGLE_PICKAXE",
        "FRACTURED_MITHRIL_PICKAXE",
        "BANDAGED_MITHRIL_PICKAXE",
        "MITHRIL_PICKAXE",
        "REFINED_MITHRIL_PICKAXE",
        "TITANIUM_PICKAXE",
        "REFINED_TITANIUM_PICKAXE",
        "PICKONIMBUS",
        "BINGONIMBUS_2000",
        "MITHRIL_DRILL_1",
        "MITHRIL_DRILL_2",
        "TITANIUM_DRILL_1",
        "TITANIUM_DRILL_2",
        "TITANIUM_DRILL_3",
        "TITANIUM_DRILL_4",
        "DIVAN_DRILL",
        "GEMSTONE_DRILL_1",
        "GEMSTONE_DRILL_2",
        "GEMSTONE_DRILL_3",
        "GEMSTONE_DRILL_4",
        "GEMSTONE_GAUNTLET"
    )

    override fun isCorrectItem(stack: ItemStack): Boolean {
        return internalMiningItemNames.contains(stack.internalName)
    }

    @Suppress("unused")
    private val skyblockJoin = listenEvent<SkyblockEnterEvent, Unit> {
        val now = Clock.System.now()
        mcScope.launch {
            delay(3.seconds)
            val cooldown = getCooldownTime() ?: return@launch
            cooldownEnd = now + (cooldown / 2)
        }
    }

    @Suppress("unused")
    private val listenChat = listenEvent<MessageEvent, Unit> {
        val raw = it.text.string
        if (raw.contains("You used your") && raw.contains("Pickaxe Ability!")) {
            abilityUsed()
            return@listenEvent
        }

        if (raw.contains("is now available!")) {
            abilityAvailable()
            return@listenEvent
        }
    }

    private fun abilityAvailable() {
        cooldownEnd = null
        if (config.mining.availableAbilityNotification) {
            if (config.mining.availableAbilityNotificationOnMiningIslands && !Mining.miningIslands.contains(
                    PlayerSessionData.currentArea
                )
            ) {
                return
            }
            minecraft.gui.setTitle(Component.literal("T")
                .withStyle { it.withObfuscated(true).withColor(ChatFormatting.WHITE) }
                .append(Component.literal(" Pickaxe Ability available ")
                    .withStyle { it.withObfuscated(false).withColor(ChatFormatting.AQUA).withBold(true) }).append(
                    Component.literal("T").withStyle { it.withObfuscated(true).withColor(ChatFormatting.WHITE) })
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
        return when (playerData.currentProfile?.mining?.abilityLevel) {
            1 -> playerData.currentProfile?.mining?.selectedAbility?.levelOne?.seconds
            2 -> playerData.currentProfile?.mining?.selectedAbility?.levelTwo?.seconds
            3 -> playerData.currentProfile?.mining?.selectedAbility?.levelThree?.seconds
            else -> null
        }
    }
}

interface AbilityCooldownIdentifier {
    @Suppress("ktlint:standard:function-naming", "FunctionName")
    fun `skylper$getCooldownPercent`(
        itemStack: ItemStack, partialTicks: Float
    ): Float
}

enum class MiningAbility(val levelOne: Int, val levelTwo: Int, val levelThree: Int) {
    MINING_SPEED_BOOST(120, 120, 120),
    PICKOBULUS(120, 110, 100),
    VEIN_SEEKER(60, 60, 60),
    MANIAC_MINER(60, 59, 59);

    companion object {
        val rawNames = listOf("Mining Speed Boost", "Pickolubus", "Vein Seeker", "Maniac Miner")

        fun byRawName(name: String): MiningAbility? {
            return when (name) {
                "Mining Speed Boost" -> MINING_SPEED_BOOST
                "Pickolubus" -> PICKOBULUS
                "Vein Seeker" -> VEIN_SEEKER
                "Maniac Miner" -> MANIAC_MINER
                else -> null
            }
        }
    }
}
