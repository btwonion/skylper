package dev.nyon.skylper.skyblock.mining.hollows.render.hud

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.render.hud.TableHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

object CrystalCompletionWidget : TableHudWidget(
    Component.translatable("menu.skylper.hollows.tabhud.crystals.title"), 5, 2
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
    override val updateTriggerEvents: List<KClass<out Event<out Any>>> = listOf(
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

        independentScope.launch {
            while (true) {
                delay(5.seconds)
                update()
            }
        }
    }
}
