package dev.nyon.skylper.extensions

import dev.nyon.skylper.mixins.LevelRendererAccessor
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.culling.Frustum

val LevelRenderer.cullingFrustum: Frustum
    get() {
        return (this as LevelRendererAccessor).cullingFrustum
    }