package dev.nyon.skylper.skyblock.render.mining.crystalHollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.BlockInteractEvent
import dev.nyon.skylper.extensions.event.BlockUpdateEvent
import dev.nyon.skylper.extensions.event.LevelChangeEvent
import dev.nyon.skylper.extensions.event.SkylperEvent
import dev.nyon.skylper.extensions.render.renderFilled
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi
import dev.nyon.skylper.skyblock.models.Area
import dev.nyon.skylper.skyblock.render.Highlighter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

object ChestHighlighter : Highlighter {
    private val foundChests = mutableMapOf<BlockPos, Instant>()
    private val mutex = Mutex()

    @Suppress("unused")
    private val removeJob = independentScope.launch {
        while (true) {
            delay(2.minutes)
            val timeStamp = Clock.System.now()
            val chestsToRemove = foundChests.filter { (_, instant) -> timeStamp - instant > 2.minutes }.keys
            chestsToRemove.forEach {
                mutex.withLock { foundChests.remove(it) }
            }
        }
    }

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun blockUpdateEvent(event: BlockUpdateEvent) {
        independentScope.launch {
            mutex.withLock { foundChests.remove(event.pos) }
            if (!config.mining.crystalHollows.highlightChests) return@launch
            if (event.state.block != Blocks.CHEST) return@launch
            delay(150.milliseconds)
            val updatedBlockState = minecraft.level?.getBlockState(event.pos)
            if (updatedBlockState?.block == Blocks.CHEST) {
                mutex.withLock {
                    foundChests[event.pos] = Clock.System.now()
                }
            }
        }
    }

    @SkylperEvent(area = Area.CRYSTAL_HOLLOWS)
    fun blockInteractionEvent(event: BlockInteractEvent) {
        if (foundChests.contains(event.result.blockPos)) independentScope.launch { foundChests.remove(event.result.blockPos) }
    }

    @SkylperEvent
    fun levelChangeEvent(event: LevelChangeEvent) { independentScope.launch { mutex.withLock { foundChests.clear() } } }

    override fun render(context: WorldRenderContext) {
        if ((!CrystalHollowsLocationApi.isPlayerInHollows || !config.mining.crystalHollows.highlightChests)) return
        mcScope.launch {
            val color = config.mining.crystalHollows.chestHighlightColor
            val copiedChests = mutex.withLock {
                foundChests
            }
            copiedChests.forEach { (pos) ->
                context.renderFilled(
                    AABB(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 1.1, pos.y + 1.1, pos.z + 1.1), color.rgb, false
                )
            }
        }
    }
}
