package dev.nyon.skylper.config.screen.extensions

import dev.isxander.yacl3.api.ButtonOption
import dev.isxander.yacl3.api.ListOption
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.controller.ValueFormatter
import dev.isxander.yacl3.gui.controllers.BooleanController
import dev.isxander.yacl3.gui.controllers.ColorController
import dev.isxander.yacl3.gui.controllers.LabelController
import dev.isxander.yacl3.gui.controllers.TickBoxController
import dev.isxander.yacl3.gui.controllers.cycling.CyclingListController
import dev.isxander.yacl3.gui.controllers.cycling.EnumController
import dev.isxander.yacl3.gui.controllers.dropdown.DropdownStringController
import dev.isxander.yacl3.gui.controllers.dropdown.EnumDropdownController
import dev.isxander.yacl3.gui.controllers.dropdown.ItemController
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController
import dev.isxander.yacl3.gui.controllers.slider.LongSliderController
import dev.isxander.yacl3.gui.controllers.string.StringController
import dev.isxander.yacl3.gui.controllers.string.number.DoubleFieldController
import dev.isxander.yacl3.gui.controllers.string.number.FloatFieldController
import dev.isxander.yacl3.gui.controllers.string.number.IntegerFieldController
import dev.isxander.yacl3.gui.controllers.string.number.LongFieldController
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.awt.Color

fun <T> Option.Builder<T>.title(
    categoryKey: String,
    key: String
): Option.Builder<T> {
    name(Component.translatable("menu.skylper.config.$categoryKey.$key.title"))
    return this
}

fun ButtonOption.Builder.title(
    categoryKey: String,
    key: String
): ButtonOption.Builder {
    name(Component.translatable("menu.skylper.config.$categoryKey.$key.title"))
    return this
}

fun <T> ListOption.Builder<T>.title(
    categoryKey: String,
    key: String
): ListOption.Builder<T> {
    name(Component.translatable("menu.skylper.config.$categoryKey.$key.title"))
    return this
}

fun Option.Builder<*>.description(
    categoryKey: String,
    key: String,
    vararg args: String
) {
    description(
        OptionDescription.of(
            Component.translatable(
                "menu.skylper.config.$categoryKey.$key.description",
                *args
            )
        )
    )
}

fun Option.Builder<*>.description(
    categoryKey: String,
    key: String,
    imageKey: ResourceLocation,
    width: Int,
    height: Int
) {
    description(
        OptionDescription.createBuilder()
            .text(Component.translatable("menu.skylper.config.$categoryKey.$key.description"))
            .image(imageKey, width, height)
            .build()
    )
}

fun Option.Builder<*>.description(block: OptionDescription.Builder.() -> Unit) {
    description(OptionDescription.createBuilder().also(block).build())
}

fun ButtonOption.Builder.description(
    categoryKey: String,
    key: String
) {
    description(OptionDescription.of(Component.translatable("menu.skylper.config.$categoryKey.$key.description")))
}

fun ButtonOption.Builder.description(
    categoryKey: String,
    key: String,
    imageKey: ResourceLocation,
    width: Int,
    height: Int
) {
    description(
        OptionDescription.createBuilder()
            .text(Component.translatable("menu.skylper.config.$categoryKey.$key.description"))
            .image(imageKey, width, height)
            .build()
    )
}

fun ButtonOption.Builder.description(block: OptionDescription.Builder.() -> Unit) {
    description(OptionDescription.createBuilder().also(block).build())
}

fun <T : Any> Option.Builder<T>.getSet(
    get: () -> T,
    set: (T) -> Unit
) {
    binding(get(), get, set)
}

fun Option.Builder<Int>.slider(
    min: Int,
    max: Int,
    interval: Int
) {
    customController {
        return@customController IntegerSliderController(it, min, max, interval)
    }
}

fun Option.Builder<Int>.field(
    min: Int,
    max: Int
) {
    customController {
        IntegerFieldController(it, min, max)
    }
}

fun Option.Builder<Double>.slider(
    min: Double,
    max: Double,
    interval: Double
) {
    customController {
        return@customController DoubleSliderController(it, min, max, interval)
    }
}

fun Option.Builder<Double>.field(
    min: Double,
    max: Double
) {
    customController {
        DoubleFieldController(it, min, max)
    }
}

fun Option.Builder<Float>.slider(
    min: Float,
    max: Float,
    interval: Float
) {
    customController {
        return@customController FloatSliderController(it, min, max, interval)
    }
}

fun Option.Builder<Float>.field(
    min: Float,
    max: Float
) {
    customController {
        FloatFieldController(it, min, max)
    }
}

fun Option.Builder<Long>.slider(
    min: Long,
    max: Long,
    interval: Long
) {
    customController {
        return@customController LongSliderController(it, min, max, interval)
    }
}

fun Option.Builder<Long>.field(
    min: Long,
    max: Long
) {
    customController {
        LongFieldController(it, min, max)
    }
}

fun Option.Builder<String>.field() {
    customController {
        return@customController StringController(it)
    }
}

fun Option.Builder<String>.dropdown(
    allowedValues: List<String>,
    emptyAllowed: Boolean = false,
    allowAny: Boolean = false
) {
    customController {
        return@customController DropdownStringController(it, allowedValues, emptyAllowed, allowAny)
    }
}

fun Option.Builder<Item>.dropdown() {
    customController {
        return@customController ItemController(it)
    }
}

fun <T : Enum<T>> Option.Builder<T>.dropdown(formatter: ValueFormatter<T>) {
    customController {
        return@customController EnumDropdownController(it, formatter)
    }
}

inline fun <reified T : Enum<T>> Option.Builder<T>.cycling(
    noinline formatter: (T) -> Component,
    allowedValues: List<T>
) {
    customController {
        return@customController EnumController(it, formatter, allowedValues.toTypedArray())
    }
}

fun <T> Option.Builder<T>.cycling(
    values: List<T>,
    formatter: (T) -> Component
) {
    customController {
        return@customController CyclingListController(it, values, formatter)
    }
}

fun Option.Builder<Boolean>.tickBox() {
    customController {
        return@customController TickBoxController(it)
    }
}

fun Option.Builder<Boolean>.default() {
    customController {
        return@customController BooleanController(it)
    }
}

fun Option.Builder<Boolean>.field(
    formatter: (Boolean) -> Component,
    colored: Boolean = true
) {
    customController {
        return@customController BooleanController(it, formatter, colored)
    }
}

fun Option.Builder<Color>.field(alphaAllowed: Boolean = true) {
    customController {
        return@customController ColorController(it, alphaAllowed)
    }
}

fun Option.Builder<Component>.label() {
    customController {
        return@customController LabelController(it)
    }
}
