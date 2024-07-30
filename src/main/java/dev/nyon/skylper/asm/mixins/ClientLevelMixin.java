package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.event.EntitySpawnEvent;
import dev.nyon.skylper.extensions.event.EventHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(
        method = "addEntity",
        at = @At("HEAD")
    )
    private void invokeEntitySpawnEvent(
        Entity entity,
        CallbackInfo ci
    ) {
        EventHandler.INSTANCE.invokeEvent(new EntitySpawnEvent(entity));
    }
}
