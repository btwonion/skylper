package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.api.ButtonOption
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.nyon.skylper.Skylper
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.event.TickEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun createLocationScreen(parent: Screen?): Screen = YetAnotherConfigLib("locations") {
    val locations by categories.registering {
        CrystalHollowsLocationApi.waypoints.forEach { loc ->
            groups.register(
                loc.specific.key, OptionGroup.createBuilder().apply {
                    name(loc.specific.displayName)
                    description(OptionDescription.of(Component.translatable("yacl3.config.locations.category.locations.description")))

                    option(ButtonOption.createBuilder()
                        .name(Component.translatable("yacl3.config.locations.category.locations.share"))
                        .description(OptionDescription.of(Component.translatable("yacl3.config.locations.category.locations.share.description")))
                        .available(PlayerSessionData.isOnSkyblock).action { _, _ ->
                            minecraft.player?.connection?.sendChat(
                                "${loc.specific.displayName.string} at ${loc.pos.x} ${loc.pos.y} ${loc.pos.z}"
                            )
                        }.build()
                    )

                    option(ButtonOption.createBuilder()
                        .name(Component.translatable("yacl3.config.locations.category.locations.delete"))
                        .description(OptionDescription.of(Component.translatable("yacl3.config.locations.category.locations.delete.description")))
                        .action { _, button ->
                            CrystalHollowsLocationApi.waypoints.remove(loc)
                            button.setAvailable(false)
                        }.build()
                    )
                }.build()
            )
        }
    }
}.generateScreen(parent)

@SkylperEvent
fun hollowsLocationKeyTickEvent(event: TickEvent) {
    if (Skylper.crystalHollowsLocationKeybinding.consumeClick()) minecraft.setScreen(createLocationScreen(null))
}
