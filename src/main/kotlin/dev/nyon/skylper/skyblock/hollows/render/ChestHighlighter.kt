package dev.nyon.skylper.skyblock.hollows.render

import de.hysky.skyblocker.utils.render.RenderHelper
import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.BlockUpdateEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.extensions.debug
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3

object ChestHighlighter {
    private val foundChests = mutableSetOf<BlockPos>()

    fun init() {
        listenForChests()
        listenForLevelChange()
        render()
    }

    private fun listenForChests() = listenEvent<BlockUpdateEvent> {
        if ((!HollowsModule.isPlayerInHollows || !config.crystalHollows.highlightChests) && !FabricLoader.getInstance().isDevelopmentEnvironment) return@listenEvent
        debug("sadasd")
        foundChests.remove(it.pos)
        if (it.state.block != Blocks.CHEST) return@listenEvent
        foundChests.add(it.pos)
    }

    private fun listenForLevelChange() = listenEvent<LevelChangeEvent> { foundChests.clear() }

    private fun render() = listenEvent<RenderAfterTranslucentEvent> {
        if ((!HollowsModule.isPlayerInHollows || !config.crystalHollows.highlightChests) && !FabricLoader.getInstance().isDevelopmentEnvironment) return@listenEvent
        val color = config.crystalHollows.chestHighlightColor
        foundChests.forEach { pos ->
            RenderHelper.renderFilled(
                it.context,
                pos,
                Vec3(1.0, 0.95, 1.0),
                color.getRGBComponents(null),
                0.3f,
                config.crystalHollows.highlightChestsThroughWalls
            )
        }
    }
}