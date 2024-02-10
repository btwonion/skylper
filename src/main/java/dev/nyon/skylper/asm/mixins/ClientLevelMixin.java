package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.BlockUpdateEvent;
import dev.nyon.skylper.extensions.EventHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(
        method = "setBlock",
        at = @At("HEAD")
    )
    private void invokeBlockUpdateEvent(
        BlockPos pos,
        BlockState state,
        int flags,
        int recursionLeft,
        CallbackInfoReturnable<Boolean> cir
    ) {
        EventHandler.INSTANCE.invokeEvent(new BlockUpdateEvent(pos, state));
    }
}
