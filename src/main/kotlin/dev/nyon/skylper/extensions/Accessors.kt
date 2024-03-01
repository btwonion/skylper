package dev.nyon.skylper.extensions

import dev.nyon.skylper.asm.accessors.GuiAccessor
import dev.nyon.skylper.asm.accessors.LevelRendererAccessor
import dev.nyon.skylper.asm.accessors.PlayerTabOverlayAccessor
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.components.PlayerTabOverlay
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.network.chat.Component
import net.minecraft.world.scores.PlayerScoreEntry

val LevelRenderer.cullingFrustum: Frustum
    get() {
        return (this as LevelRendererAccessor).cullingFrustum
    }

val PlayerTabOverlay.footer: Component
    get() {
        return (this as PlayerTabOverlayAccessor).footer ?: Component.empty()
    }

val Gui.orderComparator: Comparator<PlayerScoreEntry>
    get() {
        return (this as GuiAccessor).scoreDisplayOrder
    }