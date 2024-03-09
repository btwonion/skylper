package dev.nyon.skylper.extensions.tracker

import dev.isxander.yacl3.api.OptionGroup
import dev.nyon.skylper.extensions.EventHandler
import kotlinx.datetime.Instant
import net.minecraft.network.chat.Component

abstract class Tracker<D : TrackerData>(val nameSpace: String, val data: D) {
    val overlayNameSpace = "menu.skylper.overlay.$nameSpace"
    var startTime: Instant? = null

    abstract fun appendConfigOptions(builder: OptionGroup.Builder, categoryKey: String): OptionGroup.Builder
    abstract fun createComponents(data: D): List<Component>
    fun init() {
        data.resetTriggers.forEach {
            EventHandler.listenEvent(it) {
                data.reset()
            }
        }
    }
}