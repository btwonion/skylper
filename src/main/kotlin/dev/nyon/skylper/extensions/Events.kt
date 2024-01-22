package dev.nyon.skylper.extensions

import dev.nyon.skylper.extensions.math.Vec3Comparable
import dev.nyon.skylper.skyblock.hollows.Crystal
import net.minecraft.core.particles.ParticleOptions

object TickEvent
data class NeuReloadEvent(val files: Map<String, String>)
data class IslandChangeEvent(val previous: String?, val next: String?)
data class ProfileChangeEvent(val previous: String?, val next: String?)
object SkyblockEnterEvent
data class CrystalFoundEvent(val crystal: Crystal)
data class ParticleSpawnEvent(val options: ParticleOptions, val pos: Vec3Comparable)