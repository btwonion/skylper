package dev.nyon.skylper.config.screen.extensions

import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

fun OptionGroup.Builder.title(
    categoryKey: String,
    key: String
): OptionGroup.Builder {
    name(Component.translatable("menu.skylper.config.$categoryKey.$key.title"))
    return this
}

fun OptionGroup.Builder.description(
    categoryKey: String,
    key: String,
    vararg args: Any
) {
    description(OptionDescription.of(Component.translatable("menu.skylper.config.$categoryKey.$key.description", args)))
}

fun OptionGroup.Builder.description(
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
