package dev.nyon.skylper.extensions

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

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