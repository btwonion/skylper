package dev.nyon.skylper.skyblock.mining.hollows.render

import dev.nyon.skylper.config.config
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.ParticleSpawnEvent
import dev.nyon.skylper.extensions.RenderAfterTranslucentEvent
import dev.nyon.skylper.extensions.render.renderFilled
import dev.nyon.skylper.independentScope
import dev.nyon.skylper.mcScope
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.mining.hollows.HollowsModule
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.time.Duration.Companion.milliseconds

object ChestParticleHighlighter {
    private val particlePositions = mutableMapOf<Vec3, Instant>()
    private val mutex = Mutex()

    @Suppress("unused")
    private val particleSpawnEvent =
        listenEvent<ParticleSpawnEvent, Unit> { event ->
            if (!HollowsModule.isPlayerInHollows) return@listenEvent
            if (!config.mining.crystalHollows.chestLockHighlight) return@listenEvent
            if (event.options.type != ParticleTypes.CRIT || event.xSpeed != 0.0 || event.ySpeed != 0.0 || event.zSpeed != 0.0) return@listenEvent
            val distance = minecraft.player?.position()?.distanceTo(event.pos)
            if (distance == null || distance > 5.0) return@listenEvent
            independentScope.launch {
                mutex.withLock {
                    particlePositions[event.pos] = Clock.System.now()
                }
            }
        }

    @Suppress("unused")
    private val renderEvent =
        listenEvent<RenderAfterTranslucentEvent, Unit> {
            if (!HollowsModule.isPlayerInHollows) return@listenEvent
            if (!config.mining.crystalHollows.chestLockHighlight) return@listenEvent
            mcScope.launch {
                val positions = mutex.withLock { particlePositions.toList() }

                val now = Clock.System.now()
                positions.forEach { (pos, instant) ->
                    if (now - instant > 500.milliseconds) mutex.withLock { particlePositions.remove(pos) }
                    val box = AABB(pos.x - 0.05, pos.y - 0.05, pos.z - 0.05, pos.x + 0.05, pos.y + 0.05, pos.z + 0.05)
                    it.context.renderFilled(box, 0xFFFFFFF)
                }
            }
        }
}
