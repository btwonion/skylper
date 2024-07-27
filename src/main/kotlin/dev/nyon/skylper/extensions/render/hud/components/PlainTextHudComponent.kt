package dev.nyon.skylper.extensions.render.hud.components

import dev.nyon.skylper.minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class PlainTextHudComponent(private val text: Component) : HudComponent {
    override val width: Int = minecraft.font.width(text)
    override val height: Int = minecraft.font.lineHeight

    override fun render(
        context: GuiGraphics, x: Int, y: Int, mouseX: Int, mouseY: Int
    ) {
        context.drawString(minecraft.font, text, x, y, 0xffffff, false)
    }
}
