package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.extensions.RenderHudEvent;
import dev.nyon.skylper.extensions.render.CustomRenderLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    @Final
    private LayeredDraw layers;

    @Inject(
        method = "<init>(Lnet/minecraft/client/Minecraft;)V",
        at = @At("TAIL")
    )
    public void onInit(
        Minecraft minecraft,
        CallbackInfo ci
    ) {
        layers.add(CustomRenderLayer.INSTANCE);
    }
}
