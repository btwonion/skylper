package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

import dev.nyon.skylper.config.Config
import dev.nyon.skylper.extensions.Event
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.math.format
import dev.nyon.skylper.extensions.tracker.Tracker
import dev.nyon.skylper.extensions.tracker.TrackerData
import kotlinx.datetime.Clock
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass

data class PowderGrindingData(
    var doublePowderActive: Boolean = false,
    var gemstone: ResourceData = ResourceData(),
    var mithril: ResourceData = ResourceData(),
    var glacite: ResourceData = ResourceData(),
    var chest: ResourceData = ResourceData()
) : TrackerData {
    data class ResourceData(var total: Int = 0, var perMinute: Int = 0, var perHour: Int = 0) {
        fun update(tracker: Tracker<PowderGrindingData>) {
            if (tracker.startTime == null) return
            val timeSpent = Clock.System.now() - tracker.startTime!!
            val spentMinutes = timeSpent.inWholeSeconds.toFloat() / 60f
            perMinute = (total / spentMinutes).toInt()
            perHour = (total / (spentMinutes / 60f)).toInt()
            PowderGrindingTracker.update()
        }

        fun updateByIncrease(
            increase: Int, tracker: Tracker<PowderGrindingData>
        ) {
            total += increase
            update(tracker)
        }

        fun getComponent(
            resourceConfig: Config.CrystalHollowsConfig.GrindingOverlay.ResourceConfig,
            tracker: Tracker<PowderGrindingData>
        ): Component? {
            if (!resourceConfig.total && !resourceConfig.perMinute && !resourceConfig.perHour) return null
            val list = buildList<Component> {
                if (resourceConfig.total) {
                    add(
                        Component.translatable(
                            "${tracker.overlayNameSpace}.total_stat", total.format()
                        )
                    )
                }
                if (resourceConfig.perMinute) {
                    add(
                        Component.translatable(
                            "${tracker.overlayNameSpace}.per_minute_stat", perMinute.format()
                        )
                    )
                }
                if (resourceConfig.perHour) {
                    add(
                        Component.translatable(
                            "${tracker.overlayNameSpace}.per_hour_stat", perHour.format()
                        )
                    )
                }
            }

            if (list.isEmpty()) return null
            val builder = Component.empty()
            list.forEachIndexed { index, component ->
                if (index > 0) builder.append(Component.literal(" - ").withStyle { it.withColor(ChatFormatting.GRAY) })
                builder.append(component.copy().withStyle { it.withColor(ChatFormatting.WHITE).withBold(false) })
            }
            return builder
        }
    }

    override val resetTriggers: List<KClass<out Event<out Any>>> = listOf(LevelChangeEvent::class)

    override fun reset() {
        doublePowderActive = false
        gemstone = ResourceData()
        mithril = ResourceData()
        chest = ResourceData()
    }
}
