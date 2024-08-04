@file:Suppress("unused")

package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.*
import dev.nyon.skylper.extensions.event.EventHandler.listenEvent
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.mining.MiningAbility
import dev.nyon.skylper.skyblock.mining.hollows.Crystal
import dev.nyon.skylper.skyblock.mining.hollows.CrystalState
import net.minecraft.world.item.ItemStack

object PlayerDataUpdater {
    fun initUpdaters() {
        profileUpdateChecker()
        crystalHollowsChecker()
        hotmChecker()
    }

    private fun profileUpdateChecker() {
        listenEvent<ProfileChangeEvent, Unit> {
            if (playerData.profiles.containsKey(next)) return@listenEvent
            playerData.profiles[next ?: return@listenEvent] = ProfileData()
        }
    }

    private fun crystalHollowsChecker() {
        listenEvent<CrystalFoundEvent, Unit> {
            currentProfile.mining.crystalHollows.crystals.find { it.crystal == crystal }?.state = CrystalState.FOUND
        }

        listenEvent<NucleusRunCompleteEvent, Unit> {
            currentProfile.mining.crystalHollows.crystals.forEach { it.state = CrystalState.NOT_FOUND }
        }

        listenEvent<CrystalPlaceEvent, Unit> {
            currentProfile.mining.crystalHollows.crystals.find { it.crystal == crystal }?.state = CrystalState.PLACED
        }
    }

    private fun hotmChecker() {
        val crystalNotFoundRegex = regex("menu.hotm.crystal.notfound")
        val crystalNotPlacedRegex = regex("menu.hotm.crystal.notplaced")
        fun updateCrystalState(item: ItemStack) {
            val lore = item.lore
            val crystalNames = Crystal.entries.map { it.displayName }
            lore.map { it.string }.forEach {
                val crystalName = crystalNames.find { c -> it.contains(c) } ?: return@forEach
                val crystal = Crystal.entries.first { c -> c.displayName == crystalName }
                val newState = when {
                    crystalNotFoundRegex.matches(it) -> CrystalState.NOT_FOUND
                    crystalNotPlacedRegex.matches(it) -> CrystalState.FOUND
                    else -> CrystalState.PLACED
                }
                currentProfile.mining.crystalHollows.crystals.first { instance -> instance.crystal == crystal }.state =
                    newState
            }
        }

        val hotmTitleRegex = regex("menu.hotm.hotm")
        val crystalHollowsCrystalsRegex = regex("menu.hotm.crystal.itemName")
        val abilitySelectedRegex = regex("menu.hotm.abilities.selected")
        val peakOfTheMountainRegex = regex("menu.hotm.potm")
        val peakOfTheMountainLevelRegex = regex("menu.hotm.potm.level")

        val mithrilPowderRegex = regex("menu.hotm.mithrilPowder")
        val gemstonePowderRegex = regex("menu.hotm.gemstonePowder")
        val glacitePowderRegex = regex("menu.hotm.glacitePowder")
        listenEvent<SetItemEvent, Unit> {
            if (!hotmTitleRegex.matches(PlayerSessionData.currentScreen?.title?.string.toString())) return@listenEvent

            val itemNameString = itemStack.nameAsString
            when {
                crystalHollowsCrystalsRegex.matches(itemNameString) -> updateCrystalState(itemStack)
                MiningAbility.rawNames.any { name -> itemNameString.contains(name) } -> {
                    val selected = itemStack.lore.any { lore -> abilitySelectedRegex.matches(lore.string) }
                    if (selected) {
                        val rawName =
                            MiningAbility.rawNames.find { name -> itemNameString.contains(name) } ?: return@listenEvent
                        currentProfile.mining.selectedAbility = MiningAbility.byRawName(rawName)
                    }
                }
                peakOfTheMountainRegex.matches(itemNameString) -> {
                    val lore = itemStack.lore
                    val firstLine = lore.first().string
                    val level = peakOfTheMountainLevelRegex.singleGroup(firstLine)?.toIntOrNull() ?: return@listenEvent
                    currentProfile.mining.abilityLevel = if (level > 1) 2 else 1
                    currentProfile.mining.peakOfTheMountain = level
                }
                hotmTitleRegex.matches(itemNameString) -> {
                    val lore = itemStack.lore.map { line -> line.string }
                    val (mithrilPowder, gemstonePowder, glacitePowder) = lore.mapNotNull { line ->
                        mithrilPowderRegex.singleGroup(line) ?: gemstonePowderRegex.singleGroup(line)
                        ?: glacitePowderRegex.singleGroup(line)
                    }
                    currentProfile.mining.mithrilPowder = mithrilPowder.doubleOrNull()?.toInt() ?: return@listenEvent
                    currentProfile.mining.gemstonePowder = gemstonePowder.doubleOrNull()?.toInt() ?: return@listenEvent
                    currentProfile.mining.glacitePowder = glacitePowder.doubleOrNull()?.toInt() ?: return@listenEvent
                }
            }
        }
    }

    private val tablistMithrilPowderRegex = regex("tablist.mining.mithril")
    private val tablistGemstonePowderRegex = regex("tablist.mining.gemstone")
    private val tablistGlacitePowderRegex = regex("tablist.mining.glacite")

    private val tablistUpdateEvent = listenEvent<TablistUpdateEvent, Unit> {
        parsePowder(cleanLines)
    }

    private val sideboardUpdateEvent = listenEvent<SideboardUpdateEvent, Unit> {
        parsePowder(cleanLines)
    }

    private fun parsePowder(lines: List<String>) {
        lines.forEach { line ->
            val mithrilPowder = tablistMithrilPowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val gemstonePowder = tablistGemstonePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val glacitePowder = tablistGlacitePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()

            if (mithrilPowder != null) {
                currentProfile.mining.mithrilPowder = mithrilPowder
                EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.MITHRIL, mithrilPowder))
            }
            if (gemstonePowder != null) {
                currentProfile.mining.gemstonePowder = gemstonePowder
                EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GEMSTONE, gemstonePowder))
            }
            if (glacitePowder != null) {
                currentProfile.mining.glacitePowder = glacitePowder
                EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GLACITE, glacitePowder))
            }
        }
    }
}
