package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.event.EventHandler;
import dev.nyon.skylper.extensions.event.MessageEvent;
import dev.nyon.skylper.skyblock.data.session.PlayerSessionData;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatListener.class)
public class ChatListenerMixin {

    @Inject(
        method = "handleSystemMessage",
        at = @At("TAIL")
    )
    private void invokeChatEvent(
        Component message,
        boolean isOverlay,
        CallbackInfo ci
    ) {
        String rawMessage = message.getString()
            .replace(PlayerSessionData.INSTANCE.getComponentFixRegex()
                .toString(), "");

        EventHandler.INSTANCE.invokeEvent(new MessageEvent(message, rawMessage));
    }
}
