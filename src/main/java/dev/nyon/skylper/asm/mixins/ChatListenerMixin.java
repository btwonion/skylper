package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.event.EventHandler;
import dev.nyon.skylper.extensions.event.MessageEvent;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <1.21
/*import dev.nyon.skylper.skyblock.data.session.PlayerSessionData;*/

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
        String rawMessage = /*? if <1.21 {*//*message.getString().replace(PlayerSessionData.INSTANCE.getComponentFixRegex().toString(), ""); *//*?} else {*/ message.getString(); /*?}*/

        EventHandler.INSTANCE.invokeEvent(new MessageEvent(message, rawMessage));
    }
}
