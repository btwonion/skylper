package dev.nyon.skylper.extensions

/*? if >=1.20.6 {*/
import dev.nyon.skylper.minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

/*?}*/

val ItemStack.nameAsString: String
    get() {
        return displayName.string.drop(1).dropLast(1).clean()
    }

val ItemStack.compoundTag: CompoundTag?
    get() {
        //? if >=1.20.6
        return get(DataComponents.CUSTOM_DATA)?.copyTag() ?: return null

        //? if <1.20.6
        /*return tag*/
    }

val ItemStack.extraAttributes: CompoundTag?
    get() {
        val compoundTag = compoundTag ?: return null
        val key = "ExtraAttributes"
        if (!compoundTag.contains(key)) return null
        return compoundTag.getCompound(key)
    }

val ItemStack.internalName: String?
    get() {
        val attributes = compoundTag ?: return null
        val key = "id"
        if (!attributes.contains(key)) return null
        return attributes.getString(key)
    }

val ItemStack.display: CompoundTag?
    get() {
        val compoundTag = compoundTag ?: return null
        val key = "display"
        if (!compoundTag.contains(key)) return null
        return compoundTag.getCompound(key)
    }

val ItemStack.lore: List<Component>
    get() {
        val lines = getTooltipLines(/*? if >=1.20.6 {*/ Item.TooltipContext.EMPTY, /*?}*/
            minecraft.player!!, TooltipFlag.ADVANCED
        )
        return lines.drop(1)
    }

val ItemStack.rawLore: List<String>
    get() {
        return lore.map { it.string.clean() }
    }