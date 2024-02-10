package dev.nyon.skylper.config

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.konfig.config.saveConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

@Suppress("SpellCheckingInspection")
internal fun createYaclScreen(parent: Screen?): Screen {
    val builder = YetAnotherConfigLib.createBuilder()

    builder.title(Component.translatable("menu.skylper.name"))

    builder.save { saveConfig(config) }
    val screen = builder.build()
    return screen.generateScreen(parent)
}