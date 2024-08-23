package dev.nyon.skylper.extensions.event

import dev.nyon.skylper.skyblock.models.Area
import dev.nyon.skylper.skyblock.models.mining.PowderType
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.ChestReward
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.Crystal
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.HollowsLocation
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
 * Interface for events. Generic C specifies the return type.
 */
interface Event<C : Any?>

/**
 * Interfaces for events, that don't need a return type
 */
interface InfoEvent : Event<Unit>

/**
 * Is invoked every tick.
 */
object TickEvent : InfoEvent

/**
 * Is invoked, when player warps to another island or logs onto Skyblock.
 */
data class AreaChangeEvent(val previous: Area?, val next: Area?) : InfoEvent

/**
 * Is invoked, when player switches profiles.
 */
data class ProfileChangeEvent(val previous: String?, val next: String?) : InfoEvent

/**
 * Is invoked, when player enters Skyblock.
 */
object SkyblockEnterEvent : InfoEvent

/**
 * Is invoked, when player leaves Skyblock.
 */
object SkyblockQuitEvent : InfoEvent

/**
 * Is invoked, when player joins Hypixel.
 */
object HypixelJoinEvent : InfoEvent

/**
 * Is invoked, when player leaves Hypixel.
 */
object HypixelQuitEvent : InfoEvent

/**
 * Is invoked, when player finds a crystal in the Crystal Hollows.
 */
data class CrystalFoundEvent(val crystal: Crystal) : InfoEvent

/**
 * Is invoked, when player places a crystal in the Crystal Hollows.
 */
data class CrystalPlaceEvent(val crystal: Crystal) : InfoEvent

/**
 * Is invoked, when player gains a Crystal Loot Bundle.
 */
object NucleusRunCompleteEvent : InfoEvent

/**
 * Is invoked, when a structure in the Crystal Hollows is found.
 */
data class LocatedHollowsStructureEvent(val location: HollowsLocation) : InfoEvent

/**
 * Is invoked, when a particle spawns in the world.
 */
data class ParticleSpawnEvent(
    val options: ParticleOptions,
    val pos: Vec3,
    val xSpeed: Double,
    val ySpeed: Double,
    val zSpeed: Double,
    val force: Boolean,
    val decreased: Boolean
) : InfoEvent

/**
 * Is invoked, when entity spawns in the world.
 */
data class EntitySpawnEvent(val entity: Entity) : InfoEvent

/**
 * Is invoked, when a new world is loading.
 */
data class LevelChangeEvent(val newLevel: ClientLevel?) : InfoEvent

/**
 * Is invoked, when player receives a message.
 */
data class MessageEvent(val text: Component, val rawText: String) : InfoEvent

/**
 * Is invoked, when a [BlockState] changes near the player.
 */
data class BlockUpdateEvent(val pos: BlockPos, val state: BlockState) : InfoEvent

/**
 * Is invoked, when a block broke.
 */
data class BlockBreakEvent(val pos: BlockPos) : InfoEvent

/**
 * Is invoked after the translucent render of Minecraft. This is a subEvent from Fabric and should be used for
 * in-world rendering.
 */
data class RenderAfterTranslucentEvent(val context: WorldRenderContext) : InfoEvent

/**
 * Is invoked, when the hud is rendered. Should be used for HUD rendering.
 */
data class RenderHudEvent(val context: GuiGraphics) : InfoEvent

/**
 * Is invoked, when a block gets interacted with.
 */
data class BlockInteractEvent(val result: BlockHitResult) : InfoEvent

/**
 * Is invoked, when Minecraft is closed.
 */
object MinecraftStopEvent : InfoEvent

/**
 * Is invoked, when a screen opens.
 */
data class ScreenOpenEvent(val screen: AbstractContainerScreen<*>) : InfoEvent

/**
 * Is invoked, when an item is being set into an opened inventory.
 */
data class SetItemEvent(val itemStack: ItemStack, val rawScreenTitle: String) : InfoEvent

/**
 * Is invoked, when an inventory is opened.
 */
data class InventoryInitEvent(val items: List<ItemStack>) : InfoEvent

/**
 * Is invoked, when the boss bar changes
 */
data class BossBarNameUpdate(val text: Component, val rawText: String) : InfoEvent

/**
 * Is invoked, when the player's powder count changes.
 */
data class PowderGainEvent(val type: PowderType, val amount: Int) : InfoEvent

/**
 * Is invoked, when the powder amount is corrected by the tablist.
 */
data object PowderAdjustedEvent : InfoEvent

/**
 * Is invoked, when a player picks a treasure chest in the Crystal Hollows.
 */
object TreasureChestPickEvent : InfoEvent

/**
 * Is invoked, when a player receives rewards from a treasure chest in the Crystal Hollows.
 */
data class TreasureChestRewardsEvent(val rewards: Map<ChestReward, Int>) : InfoEvent

/**
 * Is invoked every second. Gives the clean and styled sideboard lines.
 */
data class SideboardUpdateEvent(val lines: List<Component>, val cleanLines: List<String>) : InfoEvent

/**
 * Is invoked every second. Gives the clean and styled tablist lines.
 */
data class TablistUpdateEvent(val lines: List<Component>, val cleanLines: List<String>) : InfoEvent

/**
 * Is invoked, when the background of an item in an inventory is rendered. Return type is the color the background
 * should become as an [Int].
 */
data class RenderItemBackgroundEvent(val title: Component, val rawTitle: String, val slot: Slot) : Event<Int?>