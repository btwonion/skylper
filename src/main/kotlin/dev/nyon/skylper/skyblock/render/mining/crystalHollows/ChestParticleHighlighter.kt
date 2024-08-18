package dev.nyon.skylper.skyblock.render.mining.crystalHollows

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.ParticleSpawnEvent
import dev.nyon.skylper.extensions.render.renderFilled
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.api.CrystalHollowsLocationApi
import dev.nyon.skylper.skyblock.render.Highlighter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.time.Duration.Companion.milliseconds

object ChestParticleHighlighter : Highlighter {
    private val particlePositions = mutableMapOf<Vec3, Instant>()
    private val mutex = Mutex()

    @Suppress("unused")
    private val particleSpawnEvent = listenInfoEvent<ParticleSpawnEvent> {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return@listenInfoEvent
        if (!config.mining.crystalHollows.chestLockHighlight) return@listenInfoEvent
        if (options.type != ParticleTypes.CRIT || xSpeed != 0.0 || ySpeed != 0.0 || zSpeed != 0.0) {
            return@listenInfoEvent
        }
        val distance = minecraft.player?.position()?.distanceTo(pos)
        if (distance == null || distance > 5.0) return@listenInfoEvent
        independentScope.launch {
            mutex.withLock {
                particlePositions[pos] = Clock.System.now()
            }
        }
    }

    @Suppress("unused")
    override fun render(context: WorldRenderContext) {
        if (!CrystalHollowsLocationApi.isPlayerInHollows) return
        if (!config.mining.crystalHollows.chestLockHighlight) return
        mcScope.launch {
            val positions = mutex.withLock { particlePositions.toList() }

            val now = Clock.System.now()
            positions.forEach { (pos, instant) ->
                if (now - instant > 500.milliseconds) mutex.withLock { particlePositions.remove(pos) }
                val box = AABB(pos.x - 0.05, pos.y - 0.05, pos.z - 0.05, pos.x + 0.05, pos.y + 0.05, pos.z + 0.05)
                context.renderFilled(box, 0xFFFFFFF)
            }
        }
    }
}
