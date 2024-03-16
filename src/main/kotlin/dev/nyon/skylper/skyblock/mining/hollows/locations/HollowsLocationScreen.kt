package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.Skylper
import dev.nyon.skylper.config.screen.extensions.action
import dev.nyon.skylper.config.screen.extensions.category
import dev.nyon.skylper.config.screen.extensions.description
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.TickEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun ConfigCategory.Builder.appendHollowsLocationScreen(categoryKey: String) {
    val screenOpenButtonKey = "open_location_screen"
    action(categoryKey, screenOpenButtonKey) {
        description(categoryKey, screenOpenButtonKey)
        action { screen, _ ->
            minecraft.setScreen(createLocationScreen(screen))
        }
    }
}

private fun createLocationScreen(parent: Screen?): Screen {
    val builder = YetAnotherConfigLib.createBuilder()
    builder.title(Component.translatable("menu.skylper.config.location_screen"))
    builder.category("location_screen.category") {
        val categoryKey = "location_screen.category"

        HollowsModule.waypoints.forEach { loc ->
            group(
                OptionGroup.createBuilder().apply {
                    name(loc.specific.displayName)
                    this@apply.description(categoryKey, "waypoint")

                    val shareKey = "waypoint.share"
                    this@apply.action(categoryKey, shareKey) {
                        this@action.description(categoryKey, shareKey)
                        this@action.available(PlayerSessionData.isOnSkyblock)
                        this@action.action { _, _ ->
                            minecraft.player?.connection?.sendChat(
                                "${loc.specific.displayName.string} at ${loc.pos.x} ${loc.pos.y} ${loc.pos.z}"
                            )
                        }
                    }

                    val deleteKey = "waypoint.delete"
                    this@apply.action(categoryKey, deleteKey) {
                        this@action.description(categoryKey, deleteKey)
                        this@action.action { _, _ ->
                            HollowsModule.waypoints.remove(loc)
                        }
                    }
                }.build()
            )
        }
    }

    return builder.build().generateScreen(parent)
}

fun registerHollowsLocationHotkey() =
    listenEvent<TickEvent, Unit> {
        if (Skylper.crystalHollowsLocationKeybinding.consumeClick()) minecraft.setScreen(createLocationScreen(null))
    }
