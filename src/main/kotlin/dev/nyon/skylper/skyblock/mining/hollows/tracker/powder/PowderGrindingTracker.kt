package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

import dev.isxander.yacl3.api.OptionGroup
import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.screen.extensions.description
import dev.nyon.skylper.config.screen.extensions.getSet
import dev.nyon.skylper.config.screen.extensions.primitive
import dev.nyon.skylper.config.screen.extensions.tickBox
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.tracker.Tracker
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
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

    @Suppress("unused")
    private val chatListener = listenEvent<MessageEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        val now = Clock.System.now()
        val raw = it.text.string
        when {
            PowderGrindingPatterns.powderStartedPattern.matches(raw) -> data.doublePowderActive = true
            PowderGrindingPatterns.powderEndedPattern.matches(raw) -> data.doublePowderActive = false
            PowderGrindingPatterns.pickedLockPattern.matches(raw) -> {
                if (startTime == null) startTime = now
                lastChestOpened = now
                data.chest.updateByIncrease(1, this@PowderGrindingTracker)
            }
        }

        ChestReward.entries.forEach { reward ->
            val matcher = reward.pattern.matcher(raw)
            if (!matcher.matches()) return@forEach
            if (startTime == null) startTime = now
            val amount = matcher.group("amount").doubleOrNull()?.toInt() ?: return@forEach
            if (lastChestOpened == null || now - lastChestOpened!! > 100.milliseconds) data.chest.updateByIncrease(
                1,
                this@PowderGrindingTracker
            )
            lastChestOpened = now
            val fixedAmount = amount * if (data.doublePowderActive) 2 else 1
            when (reward) {
                ChestReward.MITHRIL_POWDER -> {
                    EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.MITHRIL, fixedAmount))
                    playerData.currentProfile?.mining?.mithrilPowder =
                        fixedAmount + (playerData.currentProfile?.mining?.mithrilPowder ?: 0)
                    data.mithril.updateByIncrease(fixedAmount, this@PowderGrindingTracker)
                }
                ChestReward.GEMSTONE_POWDER -> {
                    EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GEMSTONE, fixedAmount))
                    playerData.currentProfile?.mining?.gemstonePowder =
                        fixedAmount + (playerData.currentProfile?.mining?.gemstonePowder ?: 0)
                    data.gemstone.updateByIncrease(fixedAmount, this@PowderGrindingTracker)
                }
                ChestReward.GLACITE_POWDER -> {
                    EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GLACITE, fixedAmount))
                    playerData.currentProfile?.mining?.glacitePowder =
                        fixedAmount + (playerData.currentProfile?.mining?.glacitePowder ?: 0)
                    data.glacite.updateByIncrease(fixedAmount, this@PowderGrindingTracker)
                }
                else -> {}
            }
        }
    }

    @Suppress("unused")
    private val bossBarListener = listenEvent<BossBarNameUpdate, Unit> { (text) ->
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        val raw = text.string
        if (!PowderGrindingPatterns.powderBossBarPattern.matches(raw)) return@listenEvent
        data.doublePowderActive = true
    }

    @Suppress("unused")
    private val updater = independentScope.launch {
        while (true) {
            delay(1.seconds)
            if (!isGrinding) {
                if (startTime != null) {
                    data.reset()
                    startTime = null
                }
                continue
            }
            data.chest.update(this@PowderGrindingTracker)
            data.mithril.update(this@PowderGrindingTracker)
            data.gemstone.update(this@PowderGrindingTracker)
        }
    }

    override fun appendConfigOptions(
        builder: OptionGroup.Builder, categoryKey: String
    ) = builder.apply {
        // opened chests toggle
        fun OptionGroup.Builder.resourceConfig(
            key: String, resourceConfig: Config.CrystalHollowsConfig.GrindingOverlay.ResourceConfig
        ) {
            val totalKey = "$key.total_stat"
            primitive(categoryKey, totalKey) {
                description(categoryKey, totalKey)
                getSet({ resourceConfig.total }, { resourceConfig.total = it })
                tickBox()
            }

            val minuteKey = "$key.minute_stat"
            primitive(categoryKey, minuteKey) {
                description(categoryKey, minuteKey)
                getSet({ resourceConfig.perMinute }, { resourceConfig.perMinute = it })
                tickBox()
            }

            val hourKey = "$key.hour_stat"
            primitive(categoryKey, hourKey) {
                description(categoryKey, hourKey)
                getSet({ resourceConfig.perHour }, { resourceConfig.perHour = it })
                tickBox()
            }
        }

        val chestKey = "chests"
        resourceConfig(chestKey, config.chests)

        val gemstoneKey = "gemstone"
        resourceConfig(gemstoneKey, config.gemstone)

        val mithrilKey = "mithril"
        resourceConfig(mithrilKey, config.mithril)

        val glaciteKey = "glacite"
        resourceConfig(glaciteKey, config.glacite)

        // double powder toggle
        val doublePowderKey = "double_powder"
        primitive(categoryKey, doublePowderKey) {
            description(categoryKey, doublePowderKey)
            getSet({ config.doublePowder }, { config.doublePowder = it })
            tickBox()
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
