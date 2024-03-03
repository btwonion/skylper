package dev.nyon.skylper.extensions.render.hud.components

import net.minecraft.client.gui.GuiGraphics

interface HudComponent {
    val width: Int
    val height: Int

    fun render(context: GuiGraphics, x: Int, y: Int, mouseX: Int, mouseY: Int)
}