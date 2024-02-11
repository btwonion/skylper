package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.extensions.RenderHudEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    public abstract DebugScreenOverlay getDebugOverlay();

    @Inject(
        method = "render", at = @At(value = "TAIL"), slice = @Slice(
        from = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V"
        )
    )
    )
    public void render(
        GuiGraphics drawContext,
        float tickDelta,
        CallbackInfo callbackInfo
    ) {
        if (getDebugOverlay().showDebugScreen()) return;
        EventHandler.INSTANCE.invokeEvent(new RenderHudEvent(drawContext));
    }
}
