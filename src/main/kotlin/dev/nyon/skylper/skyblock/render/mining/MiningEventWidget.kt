package dev.nyon.skylper.skyblock.render.mining

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.math.toPrettyString
import dev.nyon.skylper.extensions.render.hud.TableHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.skyblock.data.api.MiningEventApi
import dev.nyon.skylper.skyblock.data.online.IslandGroups
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import kotlinx.datetime.Clock
import net.minecraft.network.chat.Component

object MiningEventWidget : TableHudWidget(Component.translatable("menu.skylper.overlay.mining.events.title"), 1, 3) {
    override var x: Double = config.mining.eventOverlay.x.toDouble()
        set(value) {
            config.mining.eventOverlay.x = value.toInt()
            field = value
        }
    override var y: Double = config.mining.eventOverlay.x.toDouble()
        set(value) {
            config.mining.eventOverlay.x = value.toInt()
            field = value
        }

    override fun update() {
        super.update()
        val data = MiningEventApi.currentEvents

        rows = data.size
        data.forEachIndexed { index, event ->
            addComponent(index, 0, PlainTextHudComponent(event.event.getDisplayName()))
            val remaining = event.endsAt - Clock.System.now()
            addComponent(
                index, 1, PlainTextHudComponent(
                    Component.translatable(
                        "menu.skylper.overlay.mining.events.ends_in", remaining.toPrettyString()
                    )
                )
            )
            addComponent(
                index, 2, PlainTextHudComponent(
                    Component.translatable(
                        "menu.skylper.overlay.mining.events.lobby_count.${if (event.lobbyCount == 1) "single" else "multiple"}",
                        event.lobbyCount
                    )
                )
            )
        }
    }

    override fun shouldRender(): Boolean {
        return IslandGroups.groups.miningEvents.contains(PlayerSessionData.currentArea) && config.mining.eventOverlay.enabled && MiningEventApi.currentEvents.isNotEmpty()
    }

    init {
        init()
    }
}