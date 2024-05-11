package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.render.cooldown.CooldownHandler;
import dev.nyon.skylper.skyblock.mining.AbilityCooldownIdentifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin implements AbilityCooldownIdentifier {

    @Shadow
    public abstract float getCooldownPercent(
        Item item,
        float partialTicks
    );

    @Override
    public float skylper$getCooldownPercent(
        @NotNull
        ItemStack itemStack,
        float partialTicks
    ) {
        var skylperPercentage = CooldownHandler.INSTANCE.getPercentage(itemStack);
        if (skylperPercentage != null) return skylperPercentage;

        return getCooldownPercent(itemStack.getItem(), partialTicks);
    }
}
