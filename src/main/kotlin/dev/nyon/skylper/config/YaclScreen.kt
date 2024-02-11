package dev.nyon.skylper.config

import dev.isxander.yacl3.api.ButtonOption
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.render.SkylperHudModifier
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

@Suppress("SpellCheckingInspection")
internal fun createYaclScreen(parent: Screen?): Screen {
    val builder = YetAnotherConfigLib.createBuilder()

    builder.title(Component.translatable("menu.skylper.name"))
    builder.category(ConfigCategory.createBuilder().appendGeneral().build())
    builder.category(ConfigCategory.createBuilder().appendCrystalHollows().build())

    builder.save { saveConfig(config) }
    val screen = builder.build()
    return screen.generateScreen(parent)
}

private fun ConfigCategory.Builder.appendGeneral(): ConfigCategory.Builder {
    name(Component.translatable("menu.skylper.config.general.title"))

    option {
        val builder = ButtonOption.createBuilder()
        builder.name(Component.translatable("menu.skylper.config.hollows.hud.title"))
        builder.description(OptionDescription.of(Component.translatable("menu.skylper.config.hollows.hud.description")))
        builder.action { screen, _ ->
            minecraft.setScreen(SkylperHudModifier(screen))
        }
        builder.build()
    }

    return this
}

private fun ConfigCategory.Builder.appendCrystalHollows(): ConfigCategory.Builder {
    name(Component.translatable("menu.skylper.config.hollows.title"))

    return this
}