package dev.nyon.skylper.skyblock.models.mining.crystalHollows

import kotlinx.serialization.Serializable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

enum class Crystal(val displayName: String, val nucleus: Boolean) {
    AMBER("Amber", true),
    AMETHYST("Amethyst", true),
    JADE("Jade", true),
    SAPPHIRE("Sapphire", true),
    TOPAZ("Topaz", true),
    JASPER("Jasper", false),
    RUBY("Ruby", false),
    OPAL("Opal", false),
    AQUAMARINE("Aquamarine", false),
    PERIDOT("Peridot", false),
    CITRINE("Citrine", false),
    ONYX("Onyx", false);

    fun associatedLocationSpecific(): PreDefinedHollowsLocationSpecific? {
        return when (this) {
            AMBER -> PreDefinedHollowsLocationSpecific.GOBLIN_QUEEN
            AMETHYST -> PreDefinedHollowsLocationSpecific.JUNGLE_TEMPLE
            JADE -> PreDefinedHollowsLocationSpecific.MINES_OF_DIVAN
            SAPPHIRE -> PreDefinedHollowsLocationSpecific.PRECURSOR_CITY
            TOPAZ -> PreDefinedHollowsLocationSpecific.KHAZAD_DUM
            else -> null
        }
    }
}

@Serializable
data class CrystalInstance(val crystal: Crystal, var state: CrystalState)

enum class CrystalState {
    NOT_FOUND,
    FOUND,
    PLACED;

    fun getHudComponent(): Component {
        return Component.translatable("menu.skylper.hollows.tabhud.crystals.${this.name.lowercase()}").withStyle(
            when (this) {
                NOT_FOUND -> ChatFormatting.DARK_RED
                FOUND -> ChatFormatting.GOLD
                PLACED -> ChatFormatting.DARK_GREEN
            }
        )
    }
}