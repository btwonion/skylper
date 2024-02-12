package dev.nyon.skylper.skyblock.hollows.render.tabhud

import de.hysky.skyblocker.skyblock.tabhud.widget.component.PlainTextComponent
import de.hysky.skyblocker.skyblock.tabhud.widget.component.TableComponent
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.CrystalFoundEvent
import dev.nyon.skylper.extensions.CrystalPlaceEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.NucleusRunCompleteEvent
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.data.skylper.playerData
import dev.nyon.skylper.skyblock.render.tabhud.SkylperWidget
import net.minecraft.network.chat.Component

private const val COLOR = 0x51D6FF

object CrystalCompletionWidget : SkylperWidget(
    Component.translatable("menu.skylper.hollows.tabhud.crystals.title").withColor(COLOR), COLOR
) {
    override var xD: Double = config.crystalHollows.crystalOverlay.x.toDouble()
        set(value) {
            val int = value.toInt()
            x = int
            config.crystalHollows.crystalOverlay.x = int
            field = value
        }

    override var yD: Double = config.crystalHollows.crystalOverlay.y.toDouble()
        set(value) {
            val int = value.toInt()
            y = int
            config.crystalHollows.crystalOverlay.y = int
            field = value
        }

    fun init() {
        x = config.crystalHollows.crystalOverlay.x
        y = config.crystalHollows.crystalOverlay.y
        listenEvent<CrystalFoundEvent> { update() }
        listenEvent<CrystalPlaceEvent> { update() }
        listenEvent<NucleusRunCompleteEvent> { update() }
        listenEvent<LevelChangeEvent> { update() }
    }

    override fun updateContent() {
        val tableComponent = TableComponent(2, 5, COLOR)

        playerData.currentProfile?.crystalHollows?.crystals?.forEachIndexed { index, instance ->
            tableComponent.addToCell(0, index, PlainTextComponent(Component.literal(instance.crystal.displayName)))
            tableComponent.addToCell(1, index, PlainTextComponent(instance.state.component))
        }

        addComponent(tableComponent)
    }
}