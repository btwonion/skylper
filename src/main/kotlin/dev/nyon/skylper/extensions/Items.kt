package dev.nyon.skylper.extensions

import dev.nyon.skylper.minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

val ItemStack.extraAttributes: CompoundTag?
    get() {
        val tag = get(DataComponents.CUSTOM_DATA)?.copyTag() ?: return null
        val key = "ExtraAttributes"
        if (!tag.contains(key)) return null
        return tag.getCompound(key)
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
        val tag = get(DataComponents.CUSTOM_DATA)?.copyTag() ?: return null
        val key = "display"
        if (!tag.contains(key)) return null
        return tag.getCompound(key)
    }

val ItemStack.lore: List<Component>
    get() {
        val lines = getTooltipLines(Item.TooltipContext.EMPTY, minecraft.player!!, TooltipFlag.ADVANCED)
        return lines.drop(1)
    }
