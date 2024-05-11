package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.skyblock.mining.AbilityCooldownIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Redirect(
        method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemCooldowns;getCooldownPercent(Lnet/minecraft/world/item/Item;F)F"
        )
    )
    private float useItemStackCooldownPercentage(
        ItemCooldowns instance,
        Item item,
        float partialTicks,
        Font font,
        ItemStack stack,
        int x,
        int y,
        @Nullable
        String text
    ) {
        return ((AbilityCooldownIdentifier) instance).skylper$getCooldownPercent(stack, minecraft.getFrameTime());
    }
}
