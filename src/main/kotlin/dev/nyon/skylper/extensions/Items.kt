package dev.nyon.skylper.extensions

import dev.nyon.skylper.minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

val ItemStack.extraAttributes: CompoundTag?
    get() {
        tag ?: return null
        val compoundTag = tag as CompoundTag
        val key = "ExtraAttributes"
        if (!compoundTag.contains(key)) return null
        return compoundTag.getCompound(key)
    }

val ItemStack.internalName: String?
    get() {
        val attributes = extraAttributes ?: return null
        val key = "id"
        if (!attributes.contains(key)) return null
        return attributes.getString(key)
    }

val ItemStack.display: CompoundTag?
    get() {
        tag ?: return null
        val compoundTag = tag as CompoundTag
        val key = "display"
        if (!compoundTag.contains(key)) return null
        return compoundTag.getCompound(key)
    }

val ItemStack.lore: List<Component>
    get() {
        val lines = getTooltipLines(minecraft.player, TooltipFlag.ADVANCED)
        return lines.drop(1)
    }
