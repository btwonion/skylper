package dev.nyon.skylper.skyblock.hollows.render.hud

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.CrystalFoundEvent
import dev.nyon.skylper.extensions.CrystalPlaceEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.NucleusRunCompleteEvent
import dev.nyon.skylper.extensions.render.hud.TableHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass

object CrystalCompletionWidget : TableHudWidget(
    Component.translatable("menu.skylper.hollows.tabhud.crystals.title")
        .withStyle { it.withColor(ChatFormatting.DARK_AQUA.color!!) }, 5, 2
) {
    override var x: Double = config.mining.crystalHollows.crystalOverlay.x.toDouble()
        set(value) {
            config.mining.crystalHollows.crystalOverlay.x = value.toInt()
            field = value
        }
    override var y: Double = config.mining.crystalHollows.crystalOverlay.y.toDouble()
        set(value) {
            config.mining.crystalHollows.crystalOverlay.y = value.toInt()
            field = value
        }
    override val updateTriggerEvents: List<KClass<out Any>> = listOf(
        CrystalFoundEvent::class, CrystalPlaceEvent::class, NucleusRunCompleteEvent::class, LevelChangeEvent::class
    )

    override fun update() {
        super.update()
        playerData.currentProfile?.mining?.crystalHollows?.crystals?.forEachIndexed { index, instance ->
            addComponent(index, 0, PlainTextHudComponent(Component.literal(instance.crystal.displayName)))
            addComponent(index, 1, PlainTextHudComponent(instance.state.component))
        }
    }

    init {
        init()
    }
}