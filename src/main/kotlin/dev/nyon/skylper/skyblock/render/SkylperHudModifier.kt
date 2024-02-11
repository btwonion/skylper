package dev.nyon.skylper.skyblock.render

import de.hysky.skyblocker.skyblock.tabhud.widget.Widget
import dev.nyon.skylper.config.config
import dev.nyon.skylper.skyblock.hollows.render.tabhud.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.render.tabhud.SkylperWidget
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import kotlin.math.max

class SkylperHudModifier(private val parent: Screen?) : Screen(Component.translatable("menu.skylper.tabhud.modifier.title")) {
    private val enabledWidgets = buildList<SkylperWidget> {
        if (config.crystalHollows.crystalOverlay.enabled) add(CrystalCompletionWidget)
    }.onEach(Widget::update)

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderBackground(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.drawCenteredString(
            minecraft!!.font,
            Component.translatable("menu.skylper.tabhud.modifier.caption"),
            width / 2,
            height / 2,
            ChatFormatting.GRAY.color!!
        )
        enabledWidgets.forEach {
            it.render(guiGraphics)
        }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        val draggedWidget = enabledWidgets.find { widget -> (mouseX >= widget.x && mouseX <= widget.x + widget.width) && (mouseY >= widget.y && mouseY <= widget.y + widget.height) }
            ?: return false
        draggedWidget.xD = max(draggedWidget.xD + dragX, 0.0)
        draggedWidget.yD = max(draggedWidget.yD + dragY, 0.0)
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun onClose() {
        minecraft?.setScreen(parent)
    }
}