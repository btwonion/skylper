package dev.nyon.skylper.extensions

import kotlin.reflect.KClass

@Suppress("unchecked_cast")
object EventHandler {
    data class EventInstance<E : Any>(val kClass: KClass<E>, val listeners: MutableList<(event: E) -> Unit>)

    val listeners = mutableListOf<EventInstance<*>>()

    private val debugIgnoredEvents: List<KClass<*>> = listOf(TickEvent::class, ParticleSpawnEvent::class)

    inline fun <reified E : Any> listenEvent(noinline callback: (event: E) -> Unit) {
        val eventClass = E::class
        if (listeners.none { it.kClass == eventClass }) listeners.add(
            EventInstance(
                eventClass, mutableListOf(callback)
            )
        )
        else (listeners.first { it.kClass == eventClass } as EventInstance<E>).listeners.add(callback)
    }

    fun <E : Any> invokeEvent(event: E) {
        (listeners.find { it.kClass == event::class } as EventInstance<E>?)?.listeners?.forEach {
            it.invoke(event)
        }

        if (!debugIgnoredEvents.contains(event::class)) debug("Invoked event $event for ${listeners.size} listeners.")
    }
}