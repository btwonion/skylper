package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.dsl.RootDsl
import dev.isxander.yacl3.dsl.descriptionBuilder
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.render.SkylperHudModifier

fun RootDsl.appendGeneralCategory() {
    val general by categories.registering {
        val hud by rootOptions.registeringButton {
            action { parent, _ ->
                minecraft.setScreen(SkylperHudModifier(parent))
            }

            descriptionBuilder {
                addDefaultText(1)
            }
        }
    }
}