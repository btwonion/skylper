package dev.nyon.skylper.skyblock.models.mining

import kotlinx.serialization.Serializable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

@Serializable
enum class MiningEventType(private val colorChar: Char) {
    GONE_WITH_THE_WIND('9'),
    BETTER_TOGETHER('d'),
    DOUBLE_POWDER('b'),
    RAFFLE('6'),
    GOBLIN_RAID('c'),
    MITHRIL_GOURMAND('b');

    fun getDisplayName(): Component {
        return Component.literal(name.replace("_", " ")).withStyle(ChatFormatting.getByCode(colorChar)!!)
    }
}