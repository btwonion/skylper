package dev.nyon.skylper.skyblock.hollows.render.hud

import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.Symbols
import dev.nyon.skylper.extensions.math.withDot
import dev.nyon.skylper.extensions.render.hud.SimpleHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.skyblock.hollows.tracker.PowderGrindingTracker
import kotlinx.datetime.Clock
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import kotlin.reflect.KClass
import kotlin.time.DurationUnit

private const val WIDGET_NAMESPACE = "menu.skylper.hollows.tabhud.stats"

object PowderGrindingWidget : SimpleHudWidget(Component.translatable("$WIDGET_NAMESPACE.title")
    .withStyle { it.withColor(ChatFormatting.DARK_AQUA.color!!).withBold(true) }) {

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
    override val updateTriggerEvents: List<KClass<out Any>> = listOf()

    override fun update() {
        super.update()
        buildList<Component> {
            if (grindingConfig.sessionTime && PowderGrindingTracker.miningStart != null) add(
                Component.translatable("$WIDGET_NAMESPACE.session_time").withStyle(Style.EMPTY.withBold(true)).append(
                    Component.literal(
                        (Clock.System.now() - PowderGrindingTracker.miningStart!!).toString(
                            DurationUnit.MINUTES, decimals = 1
                        )
                    ).withStyle(Style.EMPTY.withBold(false))
                )
            )

            if (grindingConfig.openedChests) add(
                Component.translatable("$WIDGET_NAMESPACE.opened_chests").withStyle(Style.EMPTY.withBold(true)).append(
                    Component.literal(PowderGrindingTracker.openedChests.toString())
                        .withStyle(Style.EMPTY.withBold(false))
                )
            )

            if (grindingConfig.doublePowder) add(
                Component.translatable("$WIDGET_NAMESPACE.double_powder").withStyle(Style.EMPTY.withBold(true)).append(
                    Component.literal(if (PowderGrindingTracker.doublePowderEnabled) Symbols.CHECK_MARK else Symbols.CROSS)
                        .withStyle(Style.EMPTY.withBold(false))
                )
            )

            if (grindingConfig.farmedMithrilPowder || grindingConfig.mithrilPowderPerMinute || grindingConfig.mithrilPowderPerHour) {
                val component = Component.empty()
                component.append(
                    Component.translatable("$WIDGET_NAMESPACE.mithril").withStyle(Style.EMPTY.withBold(true))
                )
                if (grindingConfig.farmedMithrilPowder) component.append(
                    Component.translatable(
                        "$WIDGET_NAMESPACE.farmed_mithril", PowderGrindingTracker.farmedMithrilPowder.withDot()
                    )
                )
                if (grindingConfig.mithrilPowderPerHour) {
                    val componentBuilder = Component.empty()
                    if (grindingConfig.farmedMithrilPowder) componentBuilder.append(" - ")
                    componentBuilder.append(
                        Component.translatable(
                            "$WIDGET_NAMESPACE.mithril_hour", PowderGrindingTracker.mithrilPowderPerHour.withDot()
                        )
                    )
                    if (grindingConfig.mithrilPowderPerMinute) componentBuilder.append(" - ").append(
                        Component.translatable(
                            "$WIDGET_NAMESPACE.mithril_minute", PowderGrindingTracker.mithrilPowderPerMinute.withDot()
                        )
                    )
                    component.append(componentBuilder)
                }

                add(component)
            }

            if (grindingConfig.farmedGemstonePowder || grindingConfig.gemstonePowderPerMinute || grindingConfig.gemstonePowderPerMinute) {
                val component = Component.empty()
                component.append(
                    Component.translatable("$WIDGET_NAMESPACE.gemstone").withStyle(Style.EMPTY.withBold(true))
                )
                if (grindingConfig.farmedGemstonePowder) component.append(
                    Component.translatable(
                        "$WIDGET_NAMESPACE.farmed_gemstone", PowderGrindingTracker.farmedGemstonePowder.withDot()
                    )
                )
                if (grindingConfig.gemstonePowderPerHour) {
                    val componentBuilder = Component.empty()
                    if (grindingConfig.farmedGemstonePowder) componentBuilder.append(" - ")
                    componentBuilder.append(
                        Component.translatable(
                            "$WIDGET_NAMESPACE.gemstone_hour", PowderGrindingTracker.gemstonePowderPerHour.withDot()
                        )
                    )
                    if (grindingConfig.gemstonePowderPerMinute) componentBuilder.append(" - ").append(
                        Component.translatable(
                            "$WIDGET_NAMESPACE.gemstone_minute", PowderGrindingTracker.gemstonePowderPerMinute.withDot()
                        )
                    )
                    component.append(componentBuilder)
                }

                add(component)
            }
        }.forEach {
            if (it == Component.empty()) return@forEach
            components.add(PlainTextHudComponent(it))
        }
    }

    init {
        init()
    }
}