package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.StringKt;
import dev.nyon.skylper.extensions.event.BossBarNameUpdate;
import dev.nyon.skylper.extensions.event.EventInvokerKt;
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
        String rawMessage = StringKt.clean(name.getString());
        EventInvokerKt.invokeEvent(new BossBarNameUpdate(name, rawMessage));
    }
}
