package dev.nyon.skylper.skyblock.mining

import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import dev.nyon.skylper.skyblock.mining.menu.CommissionHighlighter

object Mining {
    fun init() {
        HollowsModule.init()
        CommissionHighlighter
    }
}
