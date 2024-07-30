package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.event.EventHandler;
import dev.nyon.skylper.extensions.event.MinecraftStopEvent;
import dev.nyon.skylper.extensions.event.TickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    public void dispatchTickEvent(CallbackInfo ci) {
        EventHandler.INSTANCE.invokeEvent(TickEvent.INSTANCE);
    }

    @Inject(
        method = "stop",
        at = @At("TAIL")
    )
    private void invokeMinecraftStopEvent(CallbackInfo ci) {
        EventHandler.INSTANCE.invokeEvent(MinecraftStopEvent.INSTANCE);
    }
}