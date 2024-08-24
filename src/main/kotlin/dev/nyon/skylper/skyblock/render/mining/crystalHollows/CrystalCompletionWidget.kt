package dev.nyon.skylper.skyblock.render.mining.crystalHollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.render.hud.TableHudWidget
import dev.nyon.skylper.extensions.render.hud.components.PlainTextHudComponent
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi
import dev.nyon.skylper.skyblock.data.api.CrystalsApi
import net.minecraft.network.chat.Component

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

    override fun update() {
        super.update()
        CrystalsApi.crystalStates.filter { it.crystal.nucleus }.forEachIndexed { index, instance ->
            addComponent(index, 0, PlainTextHudComponent(Component.literal(instance.crystal.displayName)))
            addComponent(index, 1, PlainTextHudComponent(instance.state.getHudComponent()))
        }
    }

    override fun shouldRender(): Boolean {
        return CrystalHollowsLocationApi.isPlayerInHollows && config.mining.crystalHollows.crystalOverlay.enabled
    }

    init {
        init()
    }
}
