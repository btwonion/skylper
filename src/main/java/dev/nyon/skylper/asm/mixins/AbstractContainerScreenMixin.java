package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.extensions.RenderItemBackgroundEvent;
import dev.nyon.skylper.extensions.ScreenOpenEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    @Shadow
    protected int imageWidth;

    @Shadow
    protected int imageHeight;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void init() {
        super.init();
        leftPos = (this.width - imageWidth) / 2;
        topPos = (this.height - imageHeight) / 2;

        EventHandler.INSTANCE.invokeEvent(new ScreenOpenEvent((AbstractContainerScreen) (Object) this));
    }

    @Inject(
        method = "renderSlot",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"
        )
    )
    private void invokeItemRenderBackgroundEvent(
        GuiGraphics guiGraphics,
        Slot slot,
        CallbackInfo ci
    ) {
        Integer callback = EventHandler.INSTANCE.invokeEvent(new RenderItemBackgroundEvent(title, slot));
        if (callback == null) return;
        guiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, callback);
    }
}