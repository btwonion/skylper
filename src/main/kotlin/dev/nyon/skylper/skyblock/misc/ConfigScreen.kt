package dev.nyon.skylper.skyblock.misc

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.category
import dev.nyon.skylper.config.screen.extensions.description
import dev.nyon.skylper.config.screen.extensions.getSet
import dev.nyon.skylper.config.screen.extensions.primitive
import dev.nyon.skylper.config.screen.extensions.tickBox

fun YetAnotherConfigLib.Builder.appendMiscCategory() =
    category("misc") {
        val categoryKey = "misc"

        val recognizeLobbyKey = "lobby_recognition"
        primitive(categoryKey, recognizeLobbyKey) {
            description(categoryKey, recognizeLobbyKey)
            getSet({ config.misc.recognizeLobbies }, { config.misc.recognizeLobbies = it })
            tickBox()
        }
    }
