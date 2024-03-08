package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.BossBarNameUpdate;
import dev.nyon.skylper.extensions.EventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossEvent.class)
public class BossEventMixin {

    @Inject(
        method = "setName",
        at = @At("HEAD")
    )
    private void invokeEvent(
        Component name,
        CallbackInfo ci
    ) {
        EventHandler.INSTANCE.invokeEvent(new BossBarNameUpdate(name));
    }
}
