package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.config.config
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
        listenEvent<ProfileChangeEvent> { (_, nextProfile) ->
            if (playerData.profiles.containsKey(nextProfile)) return@listenEvent
            playerData.profiles[nextProfile ?: return@listenEvent] = ProfileData()
        }
    }

    private fun crystalHollowsChecker() {
        listenEvent<CrystalFoundEvent> { (crystal) ->
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state =
                CrystalState.FOUND
        }

        listenEvent<NucleusRunCompleteEvent> {
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.forEach { it.state = CrystalState.NOT_FOUND }
        }

        listenEvent<CrystalPlaceEvent> { (crystal) ->
            playerData.currentProfile?.mining?.crystalHollows?.crystals?.find { it.crystal == crystal }?.state =
                CrystalState.PLACED
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
                    if (it.contains("Not Found")) CrystalState.NOT_FOUND else if (it.contains("Not Placed")) CrystalState.FOUND else CrystalState.PLACED
            }
        }

        val validScreenTitle = "Heart of the Mountain"
        listenEvent<SetItemEvent> {
            if (PlayerSessionData.currentScreen?.title?.string?.contains(validScreenTitle) == false) return@listenEvent

            val itemNameString = it.itemStack.displayName.string
            when {
                itemNameString.contains("Crystal Hollows Crystals") -> updateCrystalState(it.itemStack)
                MiningAbility.rawNames.any { name -> itemNameString.contains(name) } -> {
                    val selected = it.itemStack.lore.any { lore -> lore.string.contains("SELECTED") }
                    if (selected) {
                        val rawName =
                            MiningAbility.rawNames.find { name -> itemNameString.contains(name) } ?: return@listenEvent
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
                    val mithrilPowder =
                        lore.find { line -> line.contains("Mithril Powder: ") }?.drop(16)?.doubleOrNull()
                    if (mithrilPowder != null) playerData.currentProfile?.mining?.mithrilPowder = mithrilPowder.toInt()
                    val gemstonePowder =
                        lore.find { line -> line.contains("Gemstone Powder: ") }?.drop(17)?.doubleOrNull()
                    if (gemstonePowder != null) playerData.currentProfile?.mining?.gemstonePowder =
                        gemstonePowder.toInt()
                }
            }
        }
    }

    @Suppress("unused")
    private val sideboardUpdateEvent = listenEvent<SideboardUpdateEvent> {
        it.cleanLines.forEach { line ->
            if (line.contains("Mithril: ")) {
                val mithril = line.drop(11).doubleOrNull()
                if (mithril != null) playerData.currentProfile?.mining?.mithrilPowder = mithril.toInt()
            }

            if (line.contains("Gemstone: ")) {
                val gemstone = line.drop(12).doubleOrNull()
                if (gemstone != null) playerData.currentProfile?.mining?.gemstonePowder = gemstone.toInt()
            }
        }
    }
}