package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.action
import dev.nyon.skylper.config.screen.extensions.category
import dev.nyon.skylper.config.screen.extensions.description
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.menu.appendMenuCategory
import dev.nyon.skylper.skyblock.mining.appendMiningCategory
import dev.nyon.skylper.skyblock.mining.hollows.appendCrystalHollowsCategory
import dev.nyon.skylper.skyblock.misc.appendMiscCategory
import dev.nyon.skylper.skyblock.render.SkylperHudModifier
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

@Suppress("SpellCheckingInspection")
internal fun createYaclScreen(parent: Screen?): Screen {
    val builder = YetAnotherConfigLib.createBuilder()

    builder.title(Component.translatable("menu.skylper.config.name"))
    builder.appendGeneralCategory()
    builder.appendMenuCategory()
    builder.appendMiningCategory()
    builder.appendCrystalHollowsCategory()
    builder.appendMiscCategory()

    builder.save { saveConfig(config) }
    val screen = builder.build()
    return screen.generateScreen(parent)
}

private fun YetAnotherConfigLib.Builder.appendGeneralCategory() = category("general") {
    val categoryKey = "general"

    val hudKey = "hud"
    action(categoryKey, hudKey) {
        description(categoryKey, hudKey)
        action { screen, _ ->
            minecraft.setScreen(SkylperHudModifier(screen))
        }
    }
}
