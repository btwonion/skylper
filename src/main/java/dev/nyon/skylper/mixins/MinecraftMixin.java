package dev.nyon.skylper.mixins;

import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.extensions.TickEvent;
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
}
