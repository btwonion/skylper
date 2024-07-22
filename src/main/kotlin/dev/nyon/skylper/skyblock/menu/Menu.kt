package dev.nyon.skylper.skyblock.menu

import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.bestiary.Bestiary
import dev.nyon.skylper.skyblock.menu.collections.Collections

object Menu {
    val clickToViewRegex = regex("menu.collections.clickToView")
    fun init() {
        Collections.init()
        Bestiary.init()
    }
}
