package dev.nyon.skylper.skyblock.hollows.render

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.BlockInteractEvent
import dev.nyon.skylper.extensions.BlockUpdateEvent
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.LevelChangeEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.extensions.render.renderOutline
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.hollows.HollowsModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

object ChestHighlighter {
    private val foundChests = mutableMapOf<BlockPos, Instant>()
    private val mutex = Mutex()

    fun init() {
        listenForChests()
        listenForLevelChange()
        listenForChestInteraction()
        render()

        independentScope.launch {
            while (true) {
                delay(2.minutes)
                val timeStamp = Clock.System.now()
                val chestsToRemove = foundChests.filter { (_, instant) -> timeStamp - instant > 2.minutes }.keys
                chestsToRemove.forEach {
                    mutex.withLock { foundChests.remove(it) }
                }
            }
        }
    }

    private fun listenForChests() = listenEvent<BlockUpdateEvent> {
        independentScope.launch {
            mutex.withLock { foundChests.remove(it.pos) }
            if ((!HollowsModule.isPlayerInHollows || !config.crystalHollows.highlightChests)) return@launch
            if (it.state.block != Blocks.CHEST) return@launch
            delay(150.milliseconds)
            val updatedBlockState = minecraft.level?.getBlockState(it.pos)
            if (updatedBlockState?.block == Blocks.CHEST) mutex.withLock {
                foundChests[it.pos] = Clock.System.now()
            }
        }
    }

    private fun listenForChestInteraction() = listenEvent<BlockInteractEvent> {
        if (foundChests.contains(it.result.blockPos)) independentScope.launch { foundChests.remove(it.result.blockPos) }
    }

    private fun listenForLevelChange() =
        listenEvent<LevelChangeEvent> { independentScope.launch { mutex.withLock { foundChests.clear() } } }

    private fun render() = listenEvent<RenderAfterTranslucentEvent> {
        if ((!HollowsModule.isPlayerInHollows || !config.crystalHollows.highlightChests)) return@listenEvent
        mcScope.launch {
            val color = config.crystalHollows.chestHighlightColor
            val copiedChests = mutex.withLock {
                foundChests
            }
            copiedChests.forEach { (pos) ->
                it.context.renderOutline(
                    AABB(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 1.1, pos.y + 1.1, pos.z + 1.1),
                    color.rgb,
                    15f,
                    false
                )
            }
        }
    }
}