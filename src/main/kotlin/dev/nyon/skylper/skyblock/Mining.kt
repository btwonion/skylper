package dev.nyon.skylper.skyblock

import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule

object Mining {
    val miningIslands = listOf("Gold Mine", "Deep Caverns", "Dwarven Mines", "Crystal Hollows")

    fun init() {
        HollowsModule.init()
    }
}
