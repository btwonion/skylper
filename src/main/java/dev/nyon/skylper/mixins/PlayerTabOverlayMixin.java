package dev.nyon.skylper.mixins;

import dev.nyon.skylper.skyblock.PlayerSessionData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Shadow
    private @Nullable Component footer;

    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    public void updateCachedFooter(
        GuiGraphics guiGraphics,
        int width,
        Scoreboard scoreboard,
        Objective objective,
        CallbackInfo ci
    ) {
        if (footer == null) return;
        PlayerSessionData.INSTANCE.setFooter(footer);
    }
}
