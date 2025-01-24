package me.quickscythe.quipt;

import me.quickscythe.Bot;
import me.quickscythe.api.guild.QuiptGuild;
import me.quickscythe.api.guild.channel.QuiptTextChannel;
import me.quickscythe.api.plugins.BotPlugin;
import me.quickscythe.api.plugins.BotPluginLoader;
import me.quickscythe.quipt.api.QuiptIntegration;
import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.api.discord.embed.Embed;
import me.quickscythe.quipt.api.utils.TaskScheduler;
import me.quickscythe.quipt.files.*;
import me.quickscythe.quipt.listeners.quipt.QuiptServerListener;

import java.awt.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class FabricIntegration extends QuiptIntegration {

    File dataFolder = new File("plugins/Quipt");
    @Override
    public void enable() {
        if(!dataFolder.exists()) dataFolder.mkdirs();
        DiscordConfig discordConfig = ConfigManager.registerConfig(this, DiscordConfig.class);
        ConfigManager.registerConfig(this, HashesConfig.class);
        ConfigManager.registerConfig(this, JenkinsConfig.class);
        ConfigManager.registerConfig(this, ResourceConfig.class);
        ConfigManager.registerConfig(this, SessionConfig.class);
        ConfigManager.registerConfig(this, TeleportationConfig.class);

        events().register(new QuiptServerListener());

        if (discordConfig.enable_bot) {
            log("Initializer", "Starting discord bot");
            TaskScheduler.scheduleAsyncTask(()->{
                Bot.start(discordConfig.json());
                File folder = new File(dataFolder(), "discord_bots");
                if (!folder.exists()) folder.mkdir();
                for(File file : folder.listFiles()){
                    System.out.println("Checking file: " + file.getName());
                    if(file.getName().endsWith(".jar")){
                        System.out.println("Loading plugin: " + file.getName());
                        BotPluginLoader loader = new BotPluginLoader();
                        BotPlugin botPlugin = loader.registerPlugin(file);
                        System.out.println("Enabling plugin: " + botPlugin.name());
                        loader.enablePlugin(botPlugin);
                        System.out.println("Plugin enabled: " + botPlugin.name());
                    }
                }
                for (QuiptGuild guild : Bot.qda().getGuilds()) {
                    for (QuiptTextChannel channel : guild.getTextChannels()) {

                        if (channel.getName().equalsIgnoreCase(discordConfig.server_status_channel) || channel.getId().equalsIgnoreCase(discordConfig.server_status_channel)) {
                            Embed embed = new Embed();
                            embed.title("Server Status");
                            embed.description("Server has started.");
                            embed.color(Color.GREEN.getRGB());
                            channel.sendMessage(embed);
                        }
                    }
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void log(String s, String s1) {

    }

    @Override
    public File dataFolder() {
        return dataFolder;
    }

    @Override
    public String name() {
        return Quipt.instance().id();
    }
}
