package me.quickscythe.quipt.listeners;

import me.quickscythe.quipt.Quipt;
import me.quickscythe.quipt.api.events.QuiptPlayerChatEvent;
import me.quickscythe.quipt.api.events.QuiptPlayerJoinEvent;
import me.quickscythe.quipt.api.events.QuiptPlayerLeaveEvent;
import me.quickscythe.quipt.utils.QuiptConversionUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerListener implements ServerLifecycleEvents.ServerStopping, ServerPlayConnectionEvents.Join, ServerPlayConnectionEvents.Disconnect, ServerMessageEvents.ChatMessage, ServerLifecycleEvents.ServerStarting, ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStopping(MinecraftServer server) {
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        QuiptPlayerJoinEvent event = new QuiptPlayerJoinEvent(QuiptConversionUtils.convert(handler.player), handler.player.getGameProfile().getName() + " joined the game");
        Quipt.instance().integration().events().handle(event);
    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        QuiptPlayerLeaveEvent event = new QuiptPlayerLeaveEvent(QuiptConversionUtils.convert(handler.player), handler.player.getGameProfile().getName() + " left the game");
        Quipt.instance().integration().events().handle(event);
    }

    @Override
    public void onServerStarting(MinecraftServer server) {
        Quipt.instance().server(server);
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
    }

    @Override
    public void onChatMessage(SignedMessage message, ServerPlayerEntity player, MessageType.Parameters parameters) {
        QuiptPlayerChatEvent event = new QuiptPlayerChatEvent(QuiptConversionUtils.convert(player), message.getContent().getLiteralString());
        Quipt.instance().integration().events().handle(event);
    }
}
