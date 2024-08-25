package dev.nyon.skylper.extensions

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import java.awt.Color

val skylperPrefix: Component = Component.empty().also {
    val skylperString = "skylper"
    val startColor = Color(0xFFB500)
    val endColor = Color(0xB91C2A)

    val stepSize = 1.0 / skylperString.length

    skylperString.forEachIndexed { index, char ->
        val percent = stepSize * index
        val inversePercent: Double = 1.0 - percent
        val color = Color(
            (startColor.red * percent + endColor.red * inversePercent).toInt(),
            (startColor.green * percent + endColor.green * inversePercent).toInt(),
            (startColor.blue * percent + endColor.blue * inversePercent).toInt()
        )

        it += Component.literal(char.toString())
            .withColor(color.red * 65536 + color.green * 256 + color.blue)
            .withStyle(ChatFormatting.BOLD)
    }

    it += Component.literal(" ")
}

fun chatTranslatable(key: String, vararg arguments: Any): Component {
    return skylperPrefix + Component.translatable(key, *arguments)
}

operator fun Component.plus(other: Component): Component {
    return this.copy().append(other)
}

operator fun MutableComponent.plusAssign(other: Component) {
    this.append(other)
}