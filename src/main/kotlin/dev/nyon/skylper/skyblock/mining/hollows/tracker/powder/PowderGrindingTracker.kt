package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

import dev.nyon.skylper.config.Config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.extensions.tracker.Tracker
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import dev.nyon.skylper.config.config as overallConfig

object PowderGrindingTracker : Tracker<PowderGrindingData>("hollows.powder_grinding", PowderGrindingData()) {
    private var lastChestOpened: Instant? = null

    val isGrinding: Boolean
        get() {
            return startTime != null && lastChestOpened != null && (Clock.System.now() - lastChestOpened!!) < 5.minutes
        }

    private val config: Config.CrystalHollowsConfig.GrindingOverlay
        get() = overallConfig.mining.crystalHollows.powderGrindingOverlay

    private val powderStartedPattern = regex("chat.hollows.tracker.powder.started")
    private val powderEndedPattern = regex("chat.hollows.tracker.powder.ended")
    private val pickedLockPattern = regex("chat.hollows.tracker.picked")
    private val powderBossBarPattern = regex("bossbar.hollows.powder")

    @Suppress("unused")
    private val chatListener = listenEvent<MessageEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        val now = Clock.System.now()
        when {
            powderStartedPattern.matches(rawText) -> data.doublePowderActive = true
            powderEndedPattern.matches(rawText) -> data.doublePowderActive = false
            pickedLockPattern.matches(rawText) -> {
                if (startTime == null) startTime = now
                lastChestOpened = now
                data.chest.updateByIncrease(1, this@PowderGrindingTracker)
            }
        }

        ChestReward.entries.forEach { reward ->
            val regex = reward.getRegex()
            if (!regex.matches(rawText)) return@forEach
            if (startTime == null) startTime = now
            val amount = regex.singleGroup(rawText)?.doubleOrNull()?.toInt() ?: return@forEach
            if (lastChestOpened == null || now - lastChestOpened!! > 100.milliseconds) data.chest.updateByIncrease(
                1, this@PowderGrindingTracker
            )
            lastChestOpened = now
            val fixedAmount = amount * if (data.doublePowderActive) 2 else 1
            when (reward) {
                ChestReward.MITHRIL_POWDER -> {
                    EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.MITHRIL, fixedAmount))
                    currentProfile.mining.mithrilPowder += fixedAmount
                    data.mithril.updateByIncrease(fixedAmount, this@PowderGrindingTracker)
                }
                ChestReward.GEMSTONE_POWDER -> {
                    EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GEMSTONE, fixedAmount))
                    currentProfile.mining.gemstonePowder += fixedAmount
                    data.gemstone.updateByIncrease(fixedAmount, this@PowderGrindingTracker)
                }
                else -> {}
            }
        }
    }

    @Suppress("unused")
    private val bossBarListener = listenEvent<BossBarNameUpdate, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        if (!powderBossBarPattern.matches(rawText)) return@listenEvent
        data.doublePowderActive = true
    }

    @Suppress("unused")
    private val updater = independentScope.launch {
        while (true) {
            delay(1.seconds)
            if (startTime == null) continue
            if (!isGrinding) {
                data.reset()
                startTime = null
                continue
            }
            data.chest.update(this@PowderGrindingTracker)
            data.mithril.update(this@PowderGrindingTracker)
            data.gemstone.update(this@PowderGrindingTracker)
        }
    }

    override fun createComponents(data: PowderGrindingData): List<Component> {
        fun finalComponent(
            key: String, component: Component
        ): Component {
            val name = Component.translatable("$overlayNameSpace.$key")
                .withStyle { it.withBold(true).withColor(ChatFormatting.AQUA) }
            return name.append(component)
        }
        return buildList {
            data.chest.getComponent(config.chests, this@PowderGrindingTracker)?.let { add(finalComponent("chest", it)) }
            data.gemstone.getComponent(config.gemstone, this@PowderGrindingTracker)
                ?.let { add(finalComponent("gemstone", it)) }
            data.mithril.getComponent(config.mithril, this@PowderGrindingTracker)
                ?.let { add(finalComponent("mithril", it)) }

            if (config.doublePowder) {
                add(
                    finalComponent("double_powder",
                        Component.literal(if (data.doublePowderActive) Symbols.CHECK_MARK else Symbols.CROSS)
                            .withStyle { it.withBold(true).withColor(ChatFormatting.WHITE) })
                )
            }
        }
    }

    override var x: Double = config.x.toDouble()
        set(value) {
            config.x = value.toInt()
            field = value
        }

    override var y: Double = config.y.toDouble()
        set(value) {
            config.y = value.toInt()
            field = value
        }

    override val updateTriggerEvents: List<KClass<out Event<out Any>>> =
        listOf(BossBarNameUpdate::class, MessageEvent::class)

    init {
        init()
    }
}
