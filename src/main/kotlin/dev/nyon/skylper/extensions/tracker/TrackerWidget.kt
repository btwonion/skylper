package dev.nyon.skylper.extensions.tracker

import dev.nyon.skylper.extensions.render.hud.SimpleHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.time.DurationUnit

abstract class TrackerWidget<D : TrackerData>(val tracker: Tracker<D>) :
    SimpleHudWidget(Component.translatable("menu.skylper.overlay.${tracker.nameSpace}.title")
        .withStyle { it.withColor(ChatFormatting.AQUA) }) {
    override fun update() {
        super.update()
        if (tracker.startTime == null) return

        val now = Clock.System.now()
        val trackerDuration = (now - tracker.startTime!!).toString(DurationUnit.HOURS, 2)
        runBlocking {
            mutex.withLock {
                components.add(PlainTextHudComponent(Component.translatable("menu.skylper.overlay.duration")
                    .withStyle { it.withColor(ChatFormatting.AQUA).withBold(true) }
                    .append(Component.literal(trackerDuration)
                        .withStyle { it.withColor(ChatFormatting.WHITE).withBold(false) })
                )
                )

                components.addAll(tracker.createComponents(tracker.data).map { PlainTextHudComponent(it) })
            }
        }
    }
}