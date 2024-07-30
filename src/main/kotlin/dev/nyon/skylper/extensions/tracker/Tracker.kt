package dev.nyon.skylper.extensions.tracker

import dev.nyon.skylper.extensions.event.Event
import dev.nyon.skylper.extensions.event.EventHandler
import dev.nyon.skylper.extensions.math.toPrettyString
import dev.nyon.skylper.extensions.render.hud.SimpleHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass

abstract class Tracker<D : TrackerData>(nameSpace: String, val data: D) :
    SimpleHudWidget(Component.translatable("menu.skylper.overlay.$nameSpace.title")) {
    val overlayNameSpace = "menu.skylper.overlay.$nameSpace"
    var startTime: Instant? = null

    abstract fun createComponents(data: D): List<Component>

    @Suppress("UNCHECKED_CAST")
    override fun init() {
        super.init()
        data.resetTriggers.map { it as KClass<out Event<Any>> }.forEach {
            EventHandler.listenEvent(it) {
                startTime = null
                data.reset()
            }
        }
    }

    override fun update() {
        super.update()
        if (startTime == null) return

        val now = Clock.System.now()
        val trackerDuration = (now - startTime!!).toPrettyString()
        runBlocking {
            mutex.withLock {
                components.add(PlainTextHudComponent(Component.translatable("menu.skylper.overlay.duration")
                    .withStyle { it.withColor(ChatFormatting.AQUA).withBold(true) }
                    .append(Component.literal(trackerDuration)
                        .withStyle { it.withColor(ChatFormatting.WHITE).withBold(false) })
                )
                )

                components.addAll(createComponents(data).map { PlainTextHudComponent(it) })
            }
        }
    }
}
