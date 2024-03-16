package dev.nyon.skylper.extensions.render.hud.components

import net.minecraft.client.gui.GuiGraphics

object EmptyHudComponent : HudComponent {
    override val width: Int = 0
    override val height: Int = 0

    override fun render(
        context: GuiGraphics,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int
    ) {
    }
}
