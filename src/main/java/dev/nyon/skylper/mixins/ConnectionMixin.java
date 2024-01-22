package dev.nyon.skylper.mixins;

import dev.nyon.skylper.extensions.IslandChangeEvent;
import dev.nyon.skylper.extensions.DebugKt;
import dev.nyon.skylper.extensions.EventHandler;
import dev.nyon.skylper.skyblock.PlayerSessionData;
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
        @Nullable PacketSendListener sendListener,
        boolean flush,
        CallbackInfo ci
    ) {
        if (!(packet instanceof ServerboundHelloPacket)) return;
        if (!(address instanceof InetSocketAddress inetSocketAddress)) return;
        if (!inetSocketAddress.getHostName()
            .contains("hypixel.net")) return;
        PlayerSessionData.INSTANCE.setOnHypixel(true);
        DebugKt.debug("Joined Hypixel!");
    }

    @Inject(
        method = "disconnect",
        at = @At("TAIL")
    )
    private void clearDestinationTargets(
        Component message,
        CallbackInfo ci
    ) {
        if (PlayerSessionData.INSTANCE.getCurrentIsland() != null)
            EventHandler.INSTANCE.invokeEvent(new IslandChangeEvent(PlayerSessionData.INSTANCE.getCurrentIsland(), null));
        PlayerSessionData.INSTANCE.setOnHypixel(false);
        PlayerSessionData.INSTANCE.setOnSkyblock(false);
        PlayerSessionData.INSTANCE.setCurrentIsland(null);
        PlayerSessionData.INSTANCE.setProfile(null);
        DebugKt.debug("Left Hypixel!");
    }
}
