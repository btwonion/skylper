package dev.nyon.skylper.config.screen

import dev.isxander.yacl3.dsl.RootDsl
import dev.isxander.yacl3.dsl.controller
import dev.isxander.yacl3.dsl.descriptionBuilder
import dev.isxander.yacl3.dsl.tickBox
import dev.nyon.skylper.config.config

fun RootDsl.appendMiscCategory() {
    val misc by categories.registering {
        val recognizeLobby by rootOptions.registering {
            binding(true, { config.misc.recognizeLobbies }, { config.misc.recognizeLobbies = it })
            controller = tickBox()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
    }
}