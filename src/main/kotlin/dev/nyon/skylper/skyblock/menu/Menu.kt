package dev.nyon.skylper.skyblock.menu

import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.menu.bestiary.BestiaryMenu
import dev.nyon.skylper.skyblock.menu.collections.CollectionsMenu
import dev.nyon.skylper.skyblock.menu.mining.MiningMenu

object Menu {
    val clickToViewRegex get() = regex("menu.clickToView")
    fun init() {
        CollectionsMenu.init()
        BestiaryMenu.init()
        MiningMenu.init()
    }
}
