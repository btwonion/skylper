package dev.nyon.skylper.extensions

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
object EventHandler {
    data class EventInstance<E : Event<C>, C : Any?>(
        val kClass: KClass<E>,
        val listeners: MutableList<(event: E) -> C?>
    )

    private val listeners = mutableListOf<EventInstance<*, *>>()

    private val debugIgnoredEvents: List<KClass<out Event<*>>> =
        listOf(
            TickEvent::class,
            EntitySpawnEvent::class,
            ParticleSpawnEvent::class,
            MessageEvent::class,
            RenderAfterTranslucentEvent::class,
            RenderHudEvent::class,
            BlockUpdateEvent::class,
            BlockInteractEvent::class,
            SetItemEvent::class,
            ScreenOpenEvent::class,
            InventoryInitEvent::class,
            BossBarNameUpdate::class,
            SideboardUpdateEvent::class,
            RenderItemBackgroundEvent::class,
            LocatedHollowsStructureEvent::class
        )

    inline fun <reified E : Event<C>, C : Any?> listenEvent(noinline callback: (event: E) -> C) {
        val eventClass = E::class
        listenEvent(eventClass, callback)
    }

    fun <E : Event<C>, C : Any?> listenEvent(
        kClass: KClass<E>,
        callback: (E) -> C
    ) {
        if (listeners.none { it.kClass == kClass }) {
            listeners.add(
                EventInstance(kClass, mutableListOf(callback))
            )
        } else {
            (listeners.first { it.kClass == kClass } as EventInstance<E, C>).listeners.add(callback)
        }
    }

    fun <E : Event<C>, C : Any?> invokeEvent(event: E): C? =
        runBlocking {
            val eventListeners = listeners.find { it.kClass == event::class } as EventInstance<E, C>?
            val callbacks =
                eventListeners?.listeners?.map {
                    it.invoke(event)
                } ?: return@runBlocking null

            if (!debugIgnoredEvents.contains(event::class)) debug("Invoked event $event for ${eventListeners.listeners.size} listeners.")
            return@runBlocking callbacks.first()
        }
}
