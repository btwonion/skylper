package dev.nyon.skylper.skyblock.misc

import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.nyon.skylper.config.config
import dev.nyon.skylper.config.screen.extensions.*

fun YetAnotherConfigLib.Builder.appendMiscCategory() = category("misc") {
    val categoryKey = "misc"

    val recognizeLobbyKey = "lobby_recognition"
    primitive(categoryKey, recognizeLobbyKey) {
        description(categoryKey, recognizeLobbyKey)
        getSet({ config.misc.recognizeLobbies }, { config.misc.recognizeLobbies = it })
        tickBox()
    }
}
