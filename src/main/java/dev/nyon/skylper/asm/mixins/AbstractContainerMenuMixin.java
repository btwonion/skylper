package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.StringKt;
import dev.nyon.skylper.extensions.event.EventHandler;
import dev.nyon.skylper.extensions.event.InventoryInitEvent;
import dev.nyon.skylper.extensions.event.SetItemEvent;
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Inject(
        method = "setItem",
        at = @At("HEAD")
    )
    private void invokeSetItemEvent(
        int slotId,
        int stateId,
        ItemStack stack,
        CallbackInfo ci
    ) {
        AbstractContainerScreen<?> screen = PlayerSessionData.INSTANCE.getCurrentScreen();
        if (screen == null) return;
        String rawTitle = StringKt.clean(screen.getTitle()
            .getString());

        EventHandler.INSTANCE.invokeEvent(new SetItemEvent(stack, rawTitle));
    }

    @Inject(
        method = "initializeContents",
        at = @At("HEAD")
    )
    private void invokeInventoryInitEvent(
        int stateId,
        List<ItemStack> items,
        ItemStack carried,
        CallbackInfo ci
    ) {
        EventHandler.INSTANCE.invokeEvent(new InventoryInitEvent(items));
    }
}
