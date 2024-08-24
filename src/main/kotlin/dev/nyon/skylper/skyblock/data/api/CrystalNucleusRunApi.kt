package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.NucleusRunCompleteEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.event.invokeEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState

object CrystalNucleusRunApi {
    private val runCompletedRegex get() = regex("chat.hollows.run.completed")

    @SkylperEvent
    fun messageEvent(event: MessageEvent) {
        event.rawText.checkRunCompleted()
    }

    private fun String.checkRunCompleted() {
        if (!runCompletedRegex.matches(this)) return
        CrystalsApi.crystalStates.forEach { it.state = CrystalState.NOT_FOUND }
        invokeEvent(NucleusRunCompleteEvent)
    }
}