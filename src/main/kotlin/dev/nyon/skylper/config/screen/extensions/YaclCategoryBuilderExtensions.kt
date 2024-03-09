package dev.nyon.skylper.config.screen.extensions

import dev.isxander.yacl3.api.*
import dev.nyon.skylper.extensions.tracker.Tracker
import dev.nyon.skylper.extensions.tracker.TrackerData
import dev.nyon.skylper.minecraft
import net.minecraft.network.chat.Component

fun ConfigCategory.Builder.title(key: String): ConfigCategory.Builder {
    name(Component.translatable("menu.skylper.config.$key.title"))
    return this
}

fun OptionAddable.action(categoryKey: String, titleKey: String, block: ButtonOption.Builder.() -> Unit) {
    option(ButtonOption.createBuilder().title(categoryKey, titleKey).also(block).build())
}

fun <T> OptionAddable.primitive(categoryKey: String, titleKey: String, block: Option.Builder<T>.() -> Unit) {
    option(Option.createBuilder<T>().title(categoryKey, titleKey).also(block).build())
}

fun <T> ConfigCategory.Builder.list(categoryKey: String, titleKey: String, block: ListOption.Builder<T>.() -> Unit) {
    group(ListOption.createBuilder<T>().title(categoryKey, titleKey).also(block).build())
}

fun ConfigCategory.Builder.subGroup(categoryKey: String, titleKey: String, block: OptionGroup.Builder.() -> Unit) {
    group(OptionGroup.createBuilder().title(categoryKey, titleKey).collapsed(true).also(block).build())
}

private const val autoKey = "auto"
private const val overlayEnabledKey = "overlay.enabled"
private const val overlayXKey = "overlay.x"
private const val overlayYKey = "overlay.y"

fun ConfigCategory.Builder.overlayConfig(
    categoryKey: String,
    titleKey: String,
    enabledGet: () -> Boolean,
    enabledSet: (Boolean) -> Unit,
    xGet: () -> Int,
    xSet: (Int) -> Unit,
    yGet: () -> Int,
    ySet: (Int) -> Unit,
    extra: OptionGroup.Builder.() -> Unit = {}
) {
    subGroup(categoryKey, titleKey) {
        description(categoryKey, titleKey)
        primitive(autoKey, overlayEnabledKey) {
            description(autoKey, overlayEnabledKey)
            getSet(enabledGet, enabledSet)
            tickBox()
        }

        primitive(autoKey, overlayXKey) {
            description(autoKey, overlayXKey)
            getSet(xGet, xSet)
            field(0, minecraft.window.width)
        }

        primitive(autoKey, overlayYKey) {
            description(autoKey, overlayYKey)
            getSet(yGet, ySet)
            field(0, minecraft.window.height)
        }

        extra()
    }
}

fun <D : TrackerData> ConfigCategory.Builder.trackerConfig(
    tracker: Tracker<D>,
    enabledGet: () -> Boolean,
    enabledSet: (Boolean) -> Unit,
    xGet: () -> Int,
    xSet: (Int) -> Unit,
    yGet: () -> Int,
    ySet: (Int) -> Unit,
) {
    overlayConfig(tracker.nameSpace, "tracker", enabledGet, enabledSet, xGet, xSet, yGet, ySet) {
        tracker.appendConfigOptions(this@overlayConfig, tracker.nameSpace)
    }
}