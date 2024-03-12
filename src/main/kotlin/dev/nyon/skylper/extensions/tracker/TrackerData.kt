package dev.nyon.skylper.extensions.tracker

import dev.nyon.skylper.extensions.Event
import kotlin.reflect.KClass

interface TrackerData {
    val resetTriggers: List<KClass<out Event<out Any>>>

    fun reset()
}
