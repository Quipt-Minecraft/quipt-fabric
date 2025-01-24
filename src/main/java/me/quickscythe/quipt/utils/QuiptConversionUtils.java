package me.quickscythe.quipt.utils;

import me.quickscythe.quipt.api.entity.QuiptPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class QuiptConversionUtils {

    public static QuiptPlayer convert(PlayerEntity player) {
        if(player == null) return null;
        return new QuiptPlayer(player.getGameProfile().getName(), player.getUuid());
    }

    public static PlayerEntity convert(MinecraftServer server, QuiptPlayer player) {
        return server.getPlayerManager().getPlayer(player.uuid());
    }
}
