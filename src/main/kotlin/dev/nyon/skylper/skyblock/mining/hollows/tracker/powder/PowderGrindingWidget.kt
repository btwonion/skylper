package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.BossBarNameUpdate
import dev.nyon.skylper.extensions.MessageEvent
import dev.nyon.skylper.extensions.tracker.TrackerWidget
import kotlin.reflect.KClass

object PowderGrindingWidget : TrackerWidget<PowderGrindingData>(PowderGrindingTracker) {

    private val grindingConfig: Config.CrystalHollowsConfig.GrindingOverlay
        get() {
            return config.mining.crystalHollows.powderGrindingOverlay
        }

    override var x: Double = grindingConfig.x.toDouble()
        set(value) {
            grindingConfig.x = value.toInt()
            field = value
        }

    override var y: Double = grindingConfig.y.toDouble()
        set(value) {
            grindingConfig.y = value.toInt()
            field = value
        }
    override val updateTriggerEvents: List<KClass<out Any>> = listOf(BossBarNameUpdate::class, MessageEvent::class)

    init {
        init()
    }
}