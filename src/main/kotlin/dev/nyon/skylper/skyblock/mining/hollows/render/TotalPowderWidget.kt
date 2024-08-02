package dev.nyon.skylper.skyblock.mining.hollows.render

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.Event
import dev.nyon.skylper.extensions.event.PowderGainEvent
import dev.nyon.skylper.extensions.math.format
import dev.nyon.skylper.extensions.render.hud.TableHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.online.IslandGroups
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

object TotalPowderWidget :
    TableHudWidget(Component.translatable("menu.skylper.overlay.hollows.total_powder.title"), 3, 2) {
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
            PlainTextHudComponent(Component.translatable("menu.skylper.overlay.hollows.total_powder.mithril")
                .withStyle { it.withColor(ChatFormatting.AQUA) })
        )
        addComponent(
            0, 1, PlainTextHudComponent(
                Component.literal(
                    currentProfile.mining.mithrilPowder.format()
                )
            )
        )

        addComponent(
            1,
            0,
            PlainTextHudComponent(Component.translatable("menu.skylper.overlay.hollows.total_powder.gemstone")
                .withStyle { it.withColor(ChatFormatting.AQUA) })
        )
        addComponent(
            1, 1, PlainTextHudComponent(
                Component.literal(
                    currentProfile.mining.gemstonePowder.format()
                )
            )
        )

        addComponent(
            2,
            0,
            PlainTextHudComponent(Component.translatable("menu.skylper.overlay.hollows.total_powder.glacite")
                .withStyle { it.withColor(ChatFormatting.AQUA) })
        )
        addComponent(
            2, 1, PlainTextHudComponent(
                Component.literal(
                    currentProfile.mining.glacitePowder.format()
                )
            )
        )
    }

    override fun shouldRender(): Boolean {
        return IslandGroups.groups.mining.contains(PlayerSessionData.currentArea) && config.mining.totalPowderOverlay.enabled
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
