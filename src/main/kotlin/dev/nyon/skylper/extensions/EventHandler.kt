package dev.nyon.skylper.extensions

import dev.nyon.skylper.mcScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
object EventHandler {
    data class EventInstance<E : Any>(val kClass: KClass<E>, val listeners: MutableList<(event: E) -> Unit>)

    val listeners = mutableListOf<EventInstance<*>>()

    private val debugIgnoredEvents: List<KClass<*>> = listOf(
        TickEvent::class,
        EntitySpawnEvent::class,
        ParticleSpawnEvent::class,
        MessageEvent::class,
        RenderAfterTranslucentEvent::class,
        RenderHudEvent::class,
        BlockUpdateEvent::class,
        BlockInteractEvent::class
    )

    inline fun <reified E : Any> listenEvent(noinline callback: (event: E) -> Unit) {
        val eventClass = E::class
        if (listeners.none { it.kClass == eventClass }) listeners.add(
            EventInstance(
                eventClass, mutableListOf(callback)
            )
        )
        else (listeners.first { it.kClass == eventClass } as EventInstance<E>).listeners.add(callback)
    }

    fun <E : Any> invokeEvent(event: E) = mcScope.launch {
        val eventListeners = listeners.find { it.kClass == event::class } as EventInstance<E>?
        eventListeners?.listeners?.forEach {
            it.invoke(event)
        }

        if (!debugIgnoredEvents.contains(event::class)) debug("Invoked event $event for ${eventListeners?.listeners?.size ?: 0} listeners.")
    }
}