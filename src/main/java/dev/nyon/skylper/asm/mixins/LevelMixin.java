package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.BlockBreakEvent;
import dev.nyon.skylper.extensions.BlockUpdateEvent;
import dev.nyon.skylper.extensions.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(
        method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
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

    @Inject(
        method = "destroyBlock", at = @At("HEAD")
    )
    private void invokeBlockBreakEvent(
        BlockPos pos,
        boolean dropBlock,
        Entity entity,
        int recursionLeft,
        CallbackInfoReturnable<Boolean> cir
    ) {
        EventHandler.INSTANCE.invokeEvent(new BlockBreakEvent(pos));
    }
}
