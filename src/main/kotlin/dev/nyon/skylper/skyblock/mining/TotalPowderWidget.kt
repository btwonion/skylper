package dev.nyon.skylper.skyblock.mining

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.Event
import dev.nyon.skylper.extensions.PowderGainEvent
import dev.nyon.skylper.extensions.format
import dev.nyon.skylper.extensions.render.hud.TableHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

object TotalPowderWidget : TableHudWidget(Component.translatable("menu.skylper.overlay.hollows.total_powder.title"), 2, 2) {
    override var x: Double = config.mining.totalPowderOverlay.x.toDouble()
        set(value) {
            config.mining.totalPowderOverlay.x = value.toInt()
            field = value
        }
    override var y: Double = config.mining.totalPowderOverlay.y.toDouble()
        set(value) {
            config.mining.totalPowderOverlay.y = value.toInt()
            field = value
        }
    override val updateTriggerEvents: List<KClass<out Event<out Any>>> = listOf(PowderGainEvent::class)

    override fun update() {
        super.update()
        addComponent(
            0,
            0,
            PlainTextHudComponent(
                Component.translatable("menu.skylper.overlay.hollows.total_powder.mithril")
                    .withStyle { it.withColor(ChatFormatting.AQUA) }
            )
        )
        addComponent(
            0,
            1,
            PlainTextHudComponent(
                Component.literal(
                    playerData.currentProfile?.mining?.mithrilPowder?.format() ?: "Open /hotm screen"
                )
            )
        )

        addComponent(
            1,
            0,
            PlainTextHudComponent(
                Component.translatable("menu.skylper.overlay.hollows.total_powder.gemstone")
                    .withStyle { it.withColor(ChatFormatting.AQUA) }
            )
        )
        addComponent(
            1,
            1,
            PlainTextHudComponent(
                Component.literal(
                    playerData.currentProfile?.mining?.gemstonePowder?.format() ?: "Open /hotm screen"
                )
            )
        )
    }

    init {
        init()

        independentScope.launch {
            while (true) {
                delay(5.seconds)
                update()
            }
        }
    }
}
