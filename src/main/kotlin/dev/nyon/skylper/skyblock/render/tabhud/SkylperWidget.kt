package dev.nyon.skylper.skyblock.render.tabhud

import de.hysky.skyblocker.skyblock.tabhud.widget.Widget
import net.minecraft.network.chat.MutableComponent

abstract class SkylperWidget(title: MutableComponent?, colorValue: Int?) : Widget(title, colorValue) {
    abstract var xD: Double
    abstract var yD: Double
}