package dev.nyon.skylper.skyblock.hollows.render.tabhud

import de.hysky.skyblocker.skyblock.tabhud.widget.component.PlainTextComponent
import dev.nyon.skylper.config.Config
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.Symbols
import dev.nyon.skylper.extensions.math.withDot
import dev.nyon.skylper.skyblock.hollows.tracker.PowderGrindingTracker
import dev.nyon.skylper.skyblock.render.tabhud.SkylperWidget
import kotlinx.datetime.Clock
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import kotlin.time.DurationUnit

private const val WIDGET_NAMESPACE = "menu.skylper.hollows.tabhud.stats"

object PowderGrindingWidget : SkylperWidget(
    Component.translatable("$WIDGET_NAMESPACE.title")
        .withStyle { it.withColor(ChatFormatting.DARK_AQUA.color!!).withBold(true) }, ChatFormatting.DARK_AQUA.color!!
) {
    private val grindingConfig: Config.CrystalHollowsConfig.GrindingOverlay
        get() {
            return config.crystalHollows.powderGrindingOverlay
        }

    override var xD: Double = grindingConfig.x.toDouble()
        set(value) {
            val int = value.toInt()
            x = int
            grindingConfig.x = int
            field = value
        }

    override var yD: Double = grindingConfig.y.toDouble()
        set(value) {
            val int = value.toInt()
            y = int
            grindingConfig.y = int
            field = value
        }

    fun init() {
        x = grindingConfig.x
        y = grindingConfig.y
    }

    override fun updateContent() {
        val components = buildList<Component> {
            if (grindingConfig.sessionTime && PowderGrindingTracker.miningStart != null) add(
                Component.translatable("$WIDGET_NAMESPACE.session_time").withStyle(Style.EMPTY.withBold(true)).append(
                    Component.literal(
                        (Clock.System.now() - PowderGrindingTracker.miningStart!!).toString(
                            DurationUnit.MINUTES,
                            decimals = 1
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
        }

        components.forEach { if (it != Component.empty()) addComponent(PlainTextComponent(it)) }
        y = 200
        x = 20
    }
}