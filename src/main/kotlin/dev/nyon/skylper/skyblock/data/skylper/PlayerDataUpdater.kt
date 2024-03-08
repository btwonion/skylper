package dev.nyon.skylper.skyblock.data.skylper

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.EventHandler.listenEvent
import dev.nyon.skylper.minecraft
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData
import dev.nyon.skylper.skyblock.hollows.Crystal
import dev.nyon.skylper.skyblock.hollows.CrystalState
import dev.nyon.skylper.skyblock.mining.MiningAbility
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

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
            val lore = item.getTooltipLines(minecraft.player, TooltipFlag.ADVANCED)
            val crystalNames = Crystal.entries.map { it.displayName }
            lore.map { it.string }.forEach {
                val crystalName = crystalNames.find { c -> it.contains(c) } ?: return@forEach
                val crystal = Crystal.entries.first { c -> c.displayName == crystalName }
                playerData.currentProfile?.mining?.crystalHollows?.crystals?.first { instance -> instance.crystal == crystal }?.state =
                    if (it.contains("Not Found")) CrystalState.NOT_FOUND else if (it.contains("Found")) CrystalState.FOUND else CrystalState.NOT_FOUND
            }
        }

        val validScreenTitle = "Heart of the Mountain"
        listenEvent<SetItemEvent> {
            if (PlayerSessionData.currentScreen?.title?.string?.contains(validScreenTitle) == false) return@listenEvent

            val itemNameString = it.itemStack.displayName.string
            when {
                itemNameString.contains("Crystal Hollows Crystals") -> updateCrystalState(it.itemStack)
                MiningAbility.rawNames.any { name -> itemNameString.contains(name) } -> {
                    val selected = it.itemStack.getTooltipLines(minecraft.player, TooltipFlag.ADVANCED)
                        .any { lore -> lore.string.contains("SELECTED") }
                    if (selected) {
                        val rawName =
                            MiningAbility.rawNames.find { name -> itemNameString.contains(name) } ?: return@listenEvent
                        playerData.currentProfile?.mining?.selectedAbility = MiningAbility.byRawName(rawName)
                    }
                }
            }
        }
    }
}