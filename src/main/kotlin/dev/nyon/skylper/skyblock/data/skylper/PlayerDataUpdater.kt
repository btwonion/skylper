package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.extensions.CrystalFoundEvent
import dev.nyon.skylper.extensions.CrystalPlaceEvent
import dev.nyon.skylper.extensions.EventHandler
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.extensions.NucleusRunCompleteEvent
import dev.nyon.skylper.extensions.PowderGainEvent
import dev.nyon.skylper.extensions.ProfileChangeEvent
import dev.nyon.skylper.extensions.SetItemEvent
import dev.nyon.skylper.extensions.SideboardUpdateEvent
import dev.nyon.skylper.extensions.doubleOrNull
import dev.nyon.skylper.extensions.lore
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
        listenEvent<ProfileChangeEvent, Unit> { (_, nextProfile) ->
            if (playerData.profiles.containsKey(nextProfile)) return@listenEvent
            playerData.profiles[nextProfile ?: return@listenEvent] = ProfileData()
        }
    }

    private fun crystalHollowsChecker() {
        listenEvent<CrystalFoundEvent, Unit> { (crystal) ->
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state = CrystalState.FOUND
        }

        listenEvent<NucleusRunCompleteEvent, Unit> {
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.forEach { it.state = CrystalState.NOT_FOUND }
        }

        listenEvent<CrystalPlaceEvent, Unit> { (crystal) ->
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state = CrystalState.PLACED
        }
    }

    private fun hotmChecker() {
        fun updateCrystalState(item: ItemStack) {
            val lore = item.lore
            val crystalNames = Crystal.entries.map { it.displayName }
            lore.map { it.string }.forEach {
                val crystalName = crystalNames.find { c -> it.contains(c) } ?: return@forEach
                val crystal = Crystal.entries.first { c -> c.displayName == crystalName }
                playerData.currentProfile?.mining?.crystalHollows?.crystals?.first { instance -> instance.crystal == crystal }?.state =
                    if (it.contains("Not Found")) {
                        CrystalState.NOT_FOUND
                    } else if (it.contains("Not Placed")) {
                        CrystalState.FOUND
                    } else {
                        CrystalState.PLACED
                    }
            }
        }

        val validScreenTitle = "Heart of the Mountain"
        listenEvent<SetItemEvent, Unit> {
            if (PlayerSessionData.currentScreen?.title?.string?.contains(validScreenTitle) == false) return@listenEvent

            val itemNameString = it.itemStack.displayName.string
            when {
                itemNameString.contains("Crystal Hollows Crystals") -> updateCrystalState(it.itemStack)
                MiningAbility.rawNames.any { name -> itemNameString.contains(name) } -> {
                    val selected = it.itemStack.lore.any { lore -> lore.string.contains("SELECTED") }
                    if (selected) {
                        val rawName = MiningAbility.rawNames.find { name -> itemNameString.contains(name) } ?: return@listenEvent
                        playerData.currentProfile?.mining?.selectedAbility = MiningAbility.byRawName(rawName)
                    }
                }
                itemNameString.contains("Peak of the Mountain") -> {
                    val lore = it.itemStack.lore
                    val firstLine = lore.first().string
                    val level = firstLine.drop(6).first().digitToIntOrNull() ?: return@listenEvent
                    playerData.currentProfile?.mining?.abilityLevel = if (level > 1) 2 else 1
                }
                itemNameString.contains("Heart of the Mountain") -> {
                    val lore = it.itemStack.lore.map { line -> line.string }
                    val mithrilPowder = lore.find { line -> line.contains("Mithril Powder: ") }?.drop(16)?.doubleOrNull()
                    if (mithrilPowder != null) playerData.currentProfile?.mining?.mithrilPowder = mithrilPowder.toInt()
                    val gemstonePowder = lore.find { line -> line.contains("Gemstone Powder: ") }?.drop(17)?.doubleOrNull()
                    if (gemstonePowder != null) playerData.currentProfile?.mining?.gemstonePowder = gemstonePowder.toInt()
                    val glacitePowder = lore.find { line -> line.contains("Glacite Powder: ") }?.drop(17)?.doubleOrNull()
                    if (glacitePowder != null) playerData.currentProfile?.mining?.glacitePowder = glacitePowder.toInt()
                }
            }
        }
    }

    @Suppress("unused")
    private val sideboardUpdateEvent =
        listenEvent<SideboardUpdateEvent, Unit> {
            it.cleanLines.forEach { line ->
                if (line.contains("Mithril: ")) {
                    val mithril = line.drop(11).doubleOrNull()?.toInt()
                    if (mithril != null) {
                        playerData.currentProfile?.mining?.mithrilPowder = mithril
                        EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.MITHRIL, mithril))
                    }
                }

                if (line.contains("Gemstone: ")) {
                    val gemstone = line.drop(12).doubleOrNull()?.toInt()
                    if (gemstone != null) {
                        playerData.currentProfile?.mining?.gemstonePowder = gemstone
                        EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GEMSTONE, gemstone))
                    }
                }

                if (line.contains("Glacite: "))
                    {
                        val glacite = line.drop(9).doubleOrNull()?.toInt()
                        if (glacite != null) {
                            playerData.currentProfile?.mining?.glacitePowder = glacite
                            EventHandler.invokeEvent(PowderGainEvent(PowderGainEvent.PowderType.GLACITE, glacite))
                        }
                    }
            }
        }
}
