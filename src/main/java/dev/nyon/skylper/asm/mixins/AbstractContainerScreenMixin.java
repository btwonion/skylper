package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.extensions.ScreenOpenEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
}