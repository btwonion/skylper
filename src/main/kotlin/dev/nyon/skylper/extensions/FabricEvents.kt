package dev.nyon.skylper.extensions

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents

object FabricEvents {
    fun listenForFabricEvents() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(WorldRenderEvents.AfterTranslucent {
            EventHandler.invokeEvent(RenderAfterTranslucentEvent(it))
        })
    }
}
