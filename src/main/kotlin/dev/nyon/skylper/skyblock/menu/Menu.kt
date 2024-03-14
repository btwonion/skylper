package dev.nyon.skylper.skyblock.menu

import dev.nyon.skylper.skyblock.menu.bestiary.Bestiary
import dev.nyon.skylper.skyblock.menu.collections.Collections

object Menu {
    fun init() {
        Collections.init()
        Bestiary.init()
    }
}
