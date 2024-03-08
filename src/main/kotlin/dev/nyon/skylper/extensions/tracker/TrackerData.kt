package dev.nyon.skylper.extensions.tracker

import kotlin.reflect.KClass

interface TrackerData {
    val resetTriggers: List<KClass<out Any>>
    fun reset()
}