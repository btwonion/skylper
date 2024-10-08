package dev.nyon.skylper.skyblock.render

import dev.nyon.konfig.config.saveConfig
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.render.hud.HudWidget
import dev.nyon.skylper.skyblock.render.mining.MiningEventWidget
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.CrystalCompletionWidget
import dev.nyon.skylper.skyblock.render.mining.crystalHollows.TotalPowderWidget
import dev.nyon.skylper.skyblock.tracker.mining.crystalHollows.powder.PowderGrindingTracker
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import kotlin.math.max

class SkylperHudModifier(private val parent: Screen?) :
    Screen(Component.translatable("menu.skylper.tabhud.modifier.title")) {
    private val enabledWidgets: List<HudWidget> = buildList {
        if (config.mining.crystalHollows.crystalOverlay.enabled) add(CrystalCompletionWidget)
        if (config.mining.crystalHollows.powderGrindingOverlay.enabled) add(PowderGrindingTracker)
        if (config.mining.totalPowderOverlay.enabled) add(TotalPowderWidget)
        if (config.mining.eventOverlay.enabled) add(MiningEventWidget)
    }.onEach(HudWidget::update)

    override fun render(
        guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderBackground(guiGraphics, mouseX, mouseY, partialTick)

        guiGraphics.drawCenteredString(
            minecraft!!.font,
            Component.translatable("menu.skylper.tabhud.modifier.caption"),
            width / 2,
            height / 2,
            ChatFormatting.GRAY.color!!
        )

        enabledWidgets.filter(HudWidget::shouldRender).forEach {
            it.render(guiGraphics, mouseX, mouseY)
        }
    }

    override fun mouseDragged(
        mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double
    ): Boolean {
        val draggedWidget = enabledWidgets.find { widget ->
            (mouseX >= widget.x && mouseX <= widget.x + widget.width) && (mouseY >= widget.y && mouseY <= widget.y + widget.height)
        } ?: return false
        draggedWidget.x = max(draggedWidget.x + dragX, 0.0)
        draggedWidget.y = max(draggedWidget.y + dragY, 0.0)
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun onClose() {
        saveConfig(config)
        minecraft?.setScreen(parent)
    }
}
