package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.event.BlockInteractEvent;
import dev.nyon.skylper.extensions.event.EventInvokerKt;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(
        method = "useItemOn",
        at = @At("HEAD")
    )
    private void invokeBlockInteractEvent(
        LocalPlayer player,
        InteractionHand hand,
        BlockHitResult result,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        EventInvokerKt.invokeEvent(new BlockInteractEvent(result));
    }
}
