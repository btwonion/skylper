package dev.nyon.skylper.config.screen.extensions

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.YetAnotherConfigLib

fun YetAnotherConfigLib.Builder.category(titleKey: String, block: ConfigCategory.Builder.() -> Unit) {
    this.category(ConfigCategory.createBuilder().title(titleKey).also(block).build())
}