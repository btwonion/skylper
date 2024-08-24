package dev.nyon.skylper.skyblock.tracker.mining.crystalHollows.powder

import dev.nyon.skylper.config.Config
import dev.nyon.skylper.extensions.Symbols
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.tracker.Tracker
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsPowderGrindingApi
import dev.nyon.skylper.skyblock.models.mining.PowderType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import dev.nyon.skylper.config.config as overallConfig

object PowderGrindingTracker : Tracker<PowderGrindingData>("hollows.powder_grinding", PowderGrindingData()) {
    private var lastChestOpened: Instant? = null

    private val isGrinding: Boolean
        get() {
            return startTime != null && lastChestOpened != null && (Clock.System.now() - lastChestOpened!!) < 5.minutes
        }

    private val config: Config.CrystalHollowsConfig.GrindingOverlay
        get() = overallConfig.mining.crystalHollows.powderGrindingOverlay

    @SkylperEvent
    fun treasureChestPickEvent(event: TreasureChestPickEvent) {
        val now = Clock.System.now()
        if (startTime == null) startTime = now
        lastChestOpened = now
        data.chest.updateByIncrease(1, this@PowderGrindingTracker)
    }

    @SkylperEvent
    fun powderGainRewardEvent(event: PowderGainEvent) {
        when (event.type) {
            PowderType.MITHRIL -> data.mithril.updateByIncrease(event.amount, this@PowderGrindingTracker)
            PowderType.GEMSTONE -> data.gemstone.updateByIncrease(event.amount, this@PowderGrindingTracker)
            else -> {}
        }
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
                        Component.literal(if (CrystalHollowsPowderGrindingApi.doublePowderActive) Symbols.CHECK_MARK else Symbols.CROSS)
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

    override fun shouldRender(): Boolean {
        return CrystalHollowsLocationApi.isPlayerInHollows && overallConfig.mining.crystalHollows.powderGrindingOverlay.enabled && isGrinding
    }

    init {
        init()
    }
}
