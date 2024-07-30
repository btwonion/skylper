package dev.nyon.skylper.extensions.event

import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.locations.HollowsLocation
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

/**
 * Interface for events and taking generic C as return type.
 */
interface Event<C : Any?>

object TickEvent : Event<Unit>

data class AreaChangeEvent(val previous: String?, val next: String?) : Event<Unit>

data class ProfileChangeEvent(val previous: String?, val next: String?) : Event<Unit>

object SkyblockEnterEvent : Event<Unit>

object SkyblockQuitEvent : Event<Unit>

object HypixelJoinEvent : Event<Unit>

object HypixelQuitEvent : Event<Unit>

data class CrystalFoundEvent(val crystal: Crystal) : Event<Unit>

data class CrystalPlaceEvent(val crystal: Crystal) : Event<Unit>

object NucleusRunCompleteEvent : Event<Unit>

data class ParticleSpawnEvent(
    val options: ParticleOptions,
    val pos: Vec3,
    val xSpeed: Double,
    val ySpeed: Double,
    val zSpeed: Double,
    val force: Boolean,
    val decreased: Boolean
) : Event<Unit>

data class EntitySpawnEvent(val entity: Entity) : Event<Unit>

data class LevelChangeEvent(val newLevel: ClientLevel?) : Event<Unit>

data class MessageEvent(val text: Component, val rawText: String) : Event<Unit>

data class BlockUpdateEvent(val pos: BlockPos, val state: BlockState) : Event<Unit>

data class BlockBreakEvent(val pos: BlockPos) : Event<Unit>

data class RenderAfterTranslucentEvent(val context: WorldRenderContext) : Event<Unit>

data class RenderHudEvent(val context: GuiGraphics) : Event<Unit>

data class BlockInteractEvent(val result: BlockHitResult) : Event<Unit>

object MinecraftStopEvent : Event<Unit>

data class ScreenOpenEvent(val screen: AbstractContainerScreen<*>) : Event<Unit>

data class SetItemEvent(val itemStack: ItemStack) : Event<Unit>

data class InventoryInitEvent(val items: List<ItemStack>) : Event<Unit>

data class BossBarNameUpdate(val text: Component, val rawText: String) : Event<Unit>

data class PowderGainEvent(val type: PowderType, val amount: Int) : Event<Unit> {
    enum class PowderType {
        GEMSTONE,
        MITHRIL,
        GLACITE
    }
}

data class SideboardUpdateEvent(val lines: List<Component>, val cleanLines: List<String>) : Event<Unit>

/**
 * Return type is the color as an [Int]
 */
data class RenderItemBackgroundEvent(val title: Component, val slot: Slot) : Event<Int?>

data class LocatedHollowsStructureEvent(val location: HollowsLocation, val override: Boolean = false) : Event<Unit>
