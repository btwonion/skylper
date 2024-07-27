package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.config
import net.minecraft.client.gui.screens.Screen

internal fun createYaclScreen(parent: Screen?): Screen = YetAnotherConfigLib("skylper") {
    appendGeneralCategory()
    appendMenuCategory()
    appendMiningCategory()
    appendHollowsCategory()
    appendMiscCategory()

    save {
        saveConfig(config)
    }
}.generateScreen(parent)
