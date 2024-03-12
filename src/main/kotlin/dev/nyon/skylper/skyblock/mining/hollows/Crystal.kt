package dev.nyon.skylper.skyblock.mining.hollows

import dev.nyon.skylper.skyblock.mining.hollows.locations.PreDefinedHollowsLocationSpecific
import kotlinx.serialization.Serializable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

@Serializable
data class CrystalInstance(val crystal: Crystal, var state: CrystalState)

enum class Crystal(val displayName: String, val internalIconName: String) {
    AMBER("Amber", "PERFECT_AMBER_GEM"),
    AMETHYST("Amethyst", "PERFECT_AMETHYST_GEM"),
    JADE("Jade", "PERFECT_JADE_GEM"),
    SAPPHIRE("Sapphire", "PERFECT_SAPPHIRE_GEM"),
    TOPAZ("Topaz", "PERFECT_TOPAZ_GEM");

    fun associatedLocationSpecific(): PreDefinedHollowsLocationSpecific {
        return when (this) {
            AMBER -> PreDefinedHollowsLocationSpecific.GOBLIN_QUEEN
            AMETHYST -> PreDefinedHollowsLocationSpecific.JUNGLE_TEMPLE
            JADE -> PreDefinedHollowsLocationSpecific.MINES_OF_DIVAN
            SAPPHIRE -> PreDefinedHollowsLocationSpecific.PRECURSOR_CITY
            TOPAZ -> PreDefinedHollowsLocationSpecific.KHAZAD_DUM
        }
    }
}

enum class CrystalState(val component: Component) {
    NOT_FOUND(
        Component.translatable("menu.skylper.hollows.tabhud.crystals.not_found").withColor(ChatFormatting.DARK_RED.color!!)
    ),
    FOUND(Component.translatable("menu.skylper.hollows.tabhud.crystals.found").withColor(ChatFormatting.GOLD.color!!)),
    PLACED(
        Component.translatable("menu.skylper.hollows.tabhud.crystals.placed").withColor(ChatFormatting.DARK_GREEN.color!!)
    )
}
