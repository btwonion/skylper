package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.event.EventHandler
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.MessageEvent
import dev.nyon.skylper.extensions.event.NucleusRunCompleteEvent
import dev.nyon.skylper.extensions.regex
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState

object CrystalNucleusRunApi {
    private val runCompletedRegex get() = regex("chat.hollows.run.completed")

    fun init() = listenInfoEvent<MessageEvent> {
        rawText.checkRunCompleted()
    }

    private fun String.checkRunCompleted() {
        if (!runCompletedRegex.matches(this)) return
        currentProfile.mining.crystalHollows.crystals.forEach { it.state = CrystalState.NOT_FOUND }
        EventHandler.invokeEvent(NucleusRunCompleteEvent)
    }
}