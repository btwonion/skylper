package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
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
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state =
                CrystalState.FOUND
        }

        listenEvent<NucleusRunCompleteEvent, Unit> {
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.forEach { it.state = CrystalState.NOT_FOUND }
        }

        listenEvent<CrystalPlaceEvent, Unit> {
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state =
                CrystalState.PLACED
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
                playerData.currentProfile?.mining?.crystalHollows?.crystals?.first { instance -> instance.crystal == crystal }?.state =
                    if (crystalNotFoundRegex.matches(it)) {
                        CrystalState.NOT_FOUND
                    } else if (crystalNotPlacedRegex.matches(it)) {
                        CrystalState.FOUND
                    } else {
                        CrystalState.PLACED
                    }
            }
        }

        val hotmTitleRegex = regex("menu.hotm.hotm")
        val crystalHollowsCrystalsRegex = regex("menu.hotm.crystal.itemName")
        val abilitySelectedRegex = regex("menu.hotm.abilities.selected")
        val peakOfTheMountainRegex = regex("menu.hotm.potm")

        val mithrilPowderRegex = regex("menu.hotm.mithrilPowder")
        val gemstonePowderRegex = regex("menu.hotm.gemstonePowder")
        val glacitePowderRegex = regex("menu.hotm.glacitePowder")
        listenEvent<SetItemEvent, Unit> {
            if (hotmTitleRegex.matches(PlayerSessionData.currentScreen?.title?.string.toString())) return@listenEvent

            val itemNameString = itemStack.displayName.string
            when {
                crystalHollowsCrystalsRegex.matches(itemNameString) -> updateCrystalState(itemStack)
                MiningAbility.rawNames.any { name -> itemNameString.contains(name) } -> {
                    val selected = itemStack.lore.any { lore -> abilitySelectedRegex.matches(lore.string) }
                    if (selected) {
                        val rawName =
                            MiningAbility.rawNames.find { name -> itemNameString.contains(name) } ?: return@listenEvent
                        playerData.currentProfile?.mining?.selectedAbility = MiningAbility.byRawName(rawName)
                    }
                }
                peakOfTheMountainRegex.matches(itemNameString) -> {
                    val lore = itemStack.lore
                    val firstLine = lore.first().string
                    val level = firstLine.drop(6).first().digitToIntOrNull() ?: return@listenEvent
                    playerData.currentProfile?.mining?.abilityLevel = if (level > 1) 2 else 1
                }
                hotmTitleRegex.matches(itemNameString) -> {
                    val lore = itemStack.lore.map { line -> line.string }
                    val (mithrilPowder, gemstonePowder, glacitePowder) = lore.mapNotNull { line ->
                        mithrilPowderRegex.singleGroup(line) ?: gemstonePowderRegex.singleGroup(line)
                        ?: glacitePowderRegex.singleGroup(line)
                    }
                    playerData.currentProfile?.mining?.mithrilPowder =
                        mithrilPowder.doubleOrNull()?.toInt() ?: return@listenEvent
                    playerData.currentProfile?.mining?.gemstonePowder =
                        gemstonePowder.doubleOrNull()?.toInt() ?: return@listenEvent
                    playerData.currentProfile?.mining?.glacitePowder =
                        glacitePowder.doubleOrNull()?.toInt() ?: return@listenEvent
                }
            }
        }
    }

    private val tablistMithrilPowderRegex = regex("tablist.mining.mithril")
    private val tablistGemstonePowderRegex = regex("tablist.mining.gemstone")
    private val tablistGlacitePowderRegex = regex("tablist.mining.glacite")

    @Suppress("unused")
    private val sideboardUpdateEvent = listenEvent<SideboardUpdateEvent, Unit> {
        cleanLines.forEach { line ->
            val mithrilPowder = tablistMithrilPowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val gemstonePowder = tablistGemstonePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()
            val glacitePowder = tablistGlacitePowderRegex.singleGroup(line)?.doubleOrNull()?.toInt()

            if (mithrilPowder != null) {
                playerData.currentProfile?.mining?.mithrilPowder = mithrilPowder
                EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.MITHRIL, mithrilPowder))
            }
            if (gemstonePowder != null) {
                playerData.currentProfile?.mining?.gemstonePowder = gemstonePowder
                EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GEMSTONE, gemstonePowder))
            }
            if (glacitePowder != null) {
                playerData.currentProfile?.mining?.glacitePowder = glacitePowder
                EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GLACITE, glacitePowder))
            }
        }
    }
}
