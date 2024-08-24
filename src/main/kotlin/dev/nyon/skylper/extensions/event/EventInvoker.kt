package dev.nyon.skylper.extensions.event

import dev.nyon.skylper.events.LoadedEvents
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.models.Area
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <E : Event<C>, C : Any?> invokeEvent(event: E): C? = runBlocking {
    val eventListeners =
        LoadedEvents.events.find { it.kClass == event::class } as? EventInstance<E, C>? ?: return@runBlocking null
    val callbacks = eventListeners.listeners.filter { it.area == Area.EVERYWHERE || PlayerSessionData.currentArea == it.area }.mapNotNull {
        it.invoke(event)
    }

    return@runBlocking callbacks.firstOrNull()
}

data class EventInstance<E : Event<C>, C : Any?>(
    val kClass: KClass<E>, val listeners: List<EventListener<E, C>>
)

interface EventListener<E : Event<C>, C : Any?> {
    val area: Area
    fun invoke(event: E): C
}
