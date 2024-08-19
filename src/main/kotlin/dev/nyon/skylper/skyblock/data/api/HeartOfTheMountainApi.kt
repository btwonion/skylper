package dev.nyon.skylper.skyblock.data.api

import dev.nyon.skylper.extensions.*
import dev.nyon.skylper.extensions.event.EventHandler.listenInfoEvent
import dev.nyon.skylper.extensions.event.SetItemEvent
import dev.nyon.skylper.skyblock.data.skylper.currentProfile
import dev.nyon.skylper.skyblock.models.mining.HeartOfTheMountain
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.Crystal
import dev.nyon.skylper.skyblock.models.mining.crystalHollows.CrystalState

object HeartOfTheMountainApi {
    val data: HeartOfTheMountain get() = currentProfile.heartOfTheMountain

    private val hotmRegex get() = regex("menu.hotm")
    private val enabledRegex get() = regex("menu.hotm.enabled")

    private val crystalsItemNameRegex get() = regex("menu.hotm.crystal.itemName")
    private val crystalNotFoundRegex get() = regex("menu.hotm.crystal.notfound")
    private val crystalFoundRegex get() = regex("menu.hotm.crystal.notplaced")

    private val mithrilPowderRegex get() = regex("menu.hotm.mithrilPowder")
    private val gemstonePowderRegex get() = regex("menu.hotm.gemstonePowder")
    private val glacitePowderRegex get() = regex("menu.hotm.glacitePowder")

    private val resetHotmRegex get() = regex("menu.hotm.resetName")
    private val resetMithrilPowderRegex get() = regex("menu.hotm.reset.mithril")
    private val resetGemstonePowderRegex get() = regex("menu.hotm.reset.gemstone")
    private val resetGlacitePowderRegex get() = regex("menu.hotm.reset.glacite")

    private val abilitySelectedRegex get() = regex("menu.hotm.abilities.selected")

    private val potmItemNameRegex get() = regex("menu.hotm.potm")
    private val potmLevelRegex get() = regex("menu.hotm.potm.level")

    private val skyMallRegex get() = regex("menu.hotm.skymall")

    private val powderBuffRegex get() = regex("menu.hotm.powderbuff")
    private val powderBuffLevelRegex get() = regex("menu.hotm.powderbuff.level")

    @Suppress("unused")
    private val inventoryInitEvent = listenInfoEvent<SetItemEvent> {
        if (!hotmRegex.matches(rawScreenTitle)) return@listenInfoEvent
        val lore = itemStack.rawLore
        val name = itemStack.nameAsString

        when {
            name.matches(hotmRegex) -> {
                lore.forEach {
                    it.parsePowderFromLine(mithrilPowderRegex, gemstonePowderRegex, glacitePowderRegex)
                }
            }

            name.matches(crystalsItemNameRegex) -> {
                lore.forEach { line ->
                    val crystal = Crystal.entries.find { line.contains(it.name) } ?: return@forEach
                    CrystalsApi.crystalStates.find { it.crystal == crystal }?.state = when {
                        line.matches(crystalNotFoundRegex) -> CrystalState.NOT_FOUND
                        line.matches(crystalFoundRegex) -> CrystalState.FOUND
                        else -> CrystalState.PLACED
                    }
                }
            }

            name.matches(resetHotmRegex) -> {
                lore.forEach {
                    it.parsePowderFromLine(resetMithrilPowderRegex, resetGemstonePowderRegex, resetGlacitePowderRegex)
                }
            }

            name.matches(potmItemNameRegex) -> {
                val level = potmLevelRegex.singleGroup(lore[1])?.toIntOrNull()
                if (level != null) data.peakOfTheMountainLevel = level
            }

            name.matches(skyMallRegex) -> {
                data.skyMall = lore.any { it.matches(enabledRegex) }
            }

            name.matches(powderBuffRegex) -> {
                var level = powderBuffLevelRegex.singleGroup(lore[1])?.toIntOrNull()
                if (lore.none { enabledRegex.matches(it) }) level = 0
                if (level != null) data.powderBuffLevel = level
            }

            lore.any { abilitySelectedRegex.matches(it) } -> {
                data.pickaxeAbility = name
            }
        }
    }

    private fun String.parsePowderFromLine(
        mithrilPowderRegex: Regex, gemstonePowderRegex: Regex, glacitePowderRegex: Regex
    ) {
        val mithrilPowder = mithrilPowderRegex.singleGroup(this)?.intOrNull()
        val gemstonePowder = gemstonePowderRegex.singleGroup(this)?.intOrNull()
        val glacitePowder = glacitePowderRegex.singleGroup(this)?.intOrNull()

        mithrilPowder?.let { data.currentMithrilPowder = it }
        gemstonePowder?.let { data.currentGemstonePowder = it }
        glacitePowder?.let { data.totalGlacitePowder = it }
    }
}