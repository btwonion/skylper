package dev.nyon.skylper.asm.mixins;

import dev.nyon.skylper.extensions.event.EventHandler;
import dev.nyon.skylper.extensions.event.HypixelJoinEvent;
import dev.nyon.skylper.extensions.event.HypixelQuitEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Shadow
    private SocketAddress address;

    @Inject(
        method = "doSendPacket",
        at = @At("TAIL")
    )
    private void checkForHypixelConnection(
        Packet<?> packet,
        @Nullable
        PacketSendListener sendListener,
        boolean flush,
        CallbackInfo ci
    ) {
        if (!(packet instanceof ServerboundHelloPacket)) return;
        if (!(address instanceof InetSocketAddress inetSocketAddress)) return;
        if (!inetSocketAddress.getHostName()
            .contains("hypixel.net")) return;
        EventHandler.INSTANCE.invokeEvent(HypixelJoinEvent.INSTANCE);
    }

    @Inject(
        method = "disconnect*",
        at = @At("TAIL")
    )
    private void clearDestinationTargets(
        Component message,
        CallbackInfo ci
    ) {
        EventHandler.INSTANCE.invokeEvent(HypixelQuitEvent.INSTANCE);
    }
}