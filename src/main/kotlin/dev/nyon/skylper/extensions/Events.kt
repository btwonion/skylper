package dev.nyon.skylper.extensions

import com.mojang.brigadier.CommandDispatcher
import dev.nyon.skylper.skyblock.hollows.Crystal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.commands.CommandBuildContext
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

object TickEvent
data class AreaChangeEvent(val previous: String?, val next: String?)
data class ProfileChangeEvent(val previous: String?, val next: String?)
object SkyblockEnterEvent
object SkyblockQuitEvent
object HypixelJoinEvent
object HypixelQuitEvent
data class CrystalFoundEvent(val crystal: Crystal)
data class CrystalPlaceEvent(val crystal: Crystal)
object NucleusRunCompleteEvent
data class ParticleSpawnEvent(val options: ParticleOptions, val pos: Vec3)
data class EntitySpawnEvent(val entity: Entity)
data class LevelChangeEvent(val newLevel: ClientLevel?)
data class MessageEvent(val text: Component)
data class BlockUpdateEvent(val pos: BlockPos, val state: BlockState)
data class BlockBreakEvent(val pos: BlockPos)
data class RenderAfterTranslucentEvent(val context: WorldRenderContext)
data class RenderHudEvent(val context: GuiGraphics)
data class BlockInteractEvent(val result: BlockHitResult)
object MinecraftStopEvent