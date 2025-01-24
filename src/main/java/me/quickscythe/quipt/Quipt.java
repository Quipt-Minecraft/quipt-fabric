package me.quickscythe.quipt;

import me.quickscythe.quipt.listeners.ServerListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;

public class Quipt implements ModInitializer {

    private static Quipt INSTANCE;

    private final String MOD_ID = "quipt";
    private FabricIntegration integration;
    private MinecraftServer server;

    public static Quipt instance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        integration = new FabricIntegration();
        integration.enable();
        ServerListener mainListener = new ServerListener();
        ServerLifecycleEvents.SERVER_STOPPING.register(mainListener);
        ServerLifecycleEvents.SERVER_STARTED.register(mainListener);
        ServerLifecycleEvents.SERVER_STARTING.register(mainListener);
        ServerPlayConnectionEvents.JOIN.register(mainListener);
        ServerPlayConnectionEvents.DISCONNECT.register(mainListener);
        ServerMessageEvents.CHAT_MESSAGE.register(mainListener);
    }

    public void server(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer server() {
        return server;
    }

    public FabricIntegration integration() {
        return integration;
    }

    public String id() {
        return MOD_ID;
    }
}
