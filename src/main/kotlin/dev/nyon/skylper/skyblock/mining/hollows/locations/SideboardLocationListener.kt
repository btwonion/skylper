package dev.nyon.skylper.skyblock.mining.hollows.locations

import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LocatedHollowsStructureEvent
import dev.nyon.skylper.extensions.SideboardUpdateEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule

object SideboardLocationListener {
    @Suppress("unused")
    private val sideboardUpdateEvent = listenEvent<SideboardUpdateEvent, Unit> { event ->
        if (!HollowsModule.isPlayerInHollows) return@listenEvent
        event.cleanLines.forEach {
            val specific = when {
                it.contains("Goblin Queen's Den") -> PreDefinedHollowsLocationSpecific.GOBLIN_QUEEN
                it.contains("Fairy Grotto") -> PreDefinedHollowsLocationSpecific.FAIRY_GROTTO
                it.contains("Khazad-dûm") -> PreDefinedHollowsLocationSpecific.KHAZAD_DUM
                else -> null
            } ?: return@forEach

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
