package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.SideboardUpdateEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule

object SideboardLocationListener {
    @Suppress("unused")
    private val sideboardUpdateEvent = listenEvent<SideboardUpdateEvent, Unit> {
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        cleanLines.forEach { line ->
            val specific = PreDefinedHollowsLocationSpecific.entries.find { it.regex.matches(line) } ?: return@forEach

            EventHandler.invokeEvent(
                LocatedHollowsStructureEvent(
                    HollowsLocation(
                        minecraft.player!!.position(), specific
                    )
                )
            )
        }
    }
}
