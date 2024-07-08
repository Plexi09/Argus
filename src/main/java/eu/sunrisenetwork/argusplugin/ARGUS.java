package eu.sunrisenetwork.argusplugin;

import eu.sunrisenetwork.argusplugin.commands.*;
import eu.sunrisenetwork.argusplugin.listeners.*;
import eu.sunrisenetwork.argusplugin.tasks.DailyResetTask;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;
import eu.sunrisenetwork.argusplugin.util.Reload;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ARGUS extends JavaPlugin {
    private File customConfigFile;
    private FileConfiguration customConfig;
    public static boolean debugEnabled;
    public static boolean spectateEnabled;
    public static boolean topluckEnabled;
    public static int topluckDataResetInterval;

    @Override
    public void onEnable() {
        long startTime = System.nanoTime();  // Start time

        MessageUtils.sendMessage(getServer().getConsoleSender(), "Starting Argus...");

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Creating custom config...");
        }
        createCustomConfig();
        
        ARGUS.debugEnabled = this.getConfig().getBoolean("debug");
        ARGUS.spectateEnabled = this.getConfig().getBoolean("commands.spectate.enabled");
        ARGUS.topluckEnabled = this.getConfig().getBoolean("commands.topluck.enabled");
        ARGUS.topluckDataResetInterval = this.getConfig().getInt("commands.topluck.player-data-reset-interval", 864000); // Default to 864000 ticks

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Debug mode is enabled.");
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Spectate command enabled: " + spectateEnabled);
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Topluck command enabled: " + topluckEnabled);
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Topluck data reset interval: " + topluckDataResetInterval);
        }

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Registering commands...");
        }

        this.getCommand("spectate").setExecutor(new CommandSpectate(this));
        
        this.getCommand("sethealth").setExecutor(new CommandSetHealth());
        
        this.getCommand("topluck").setExecutor(new CommandTopLuck(this));

        this.getCommand("openinv").setExecutor(new CommandOpeninv(this));
        this.getCommand("oi").setExecutor(new CommandOpeninv(this));
        this.getCommand("inv").setExecutor(new CommandOpeninv(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryClickListener(), this);

        this.getCommand("openender").setExecutor(new CommandOpenender());
        this.getCommand("oe").setExecutor(new CommandOpenender());

        this.getCommand("searchinv").setExecutor(new CommandSearchinv());
        this.getCommand("searchender").setExecutor(new CommandSearchender());

        this.getCommand("silentcontainer").setExecutor(new CommandSilentcontainer());
        this.getCommand("silentc").setExecutor(new CommandSilentcontainer());
        this.getCommand("silentchest").setExecutor(new CommandSilentcontainer());

        this.getCommand("arrest").setExecutor(new CommandArrest(this));
        this.getCommand("argusreload").setExecutor(new Reload(this));
        
        this.getCommand("staffchat").setExecutor(new CommandStaffChat(this));
        this.getCommand("staffc").setExecutor(new CommandStaffChat(this));
        this.getCommand("schat").setExecutor(new CommandStaffChat(this));
        this.getCommand("sc").setExecutor(new CommandStaffChat(this));	

        getServer().getPluginManager().registerEvents(new TopLuckInventoryListener(), this);

        long endTime = System.nanoTime();  // End time
        long duration = (endTime - startTime) / 1000000;  // Duration in milliseconds

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Finished loading in " + duration + " ms!");
        } else {
            MessageUtils.sendMessage(getServer().getConsoleSender(), "Finished loading!");
        }
        
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Thank you for using Argus!");

        // Start the daily reset task if topluck is enabled
        if (topluckEnabled) {
            new DailyResetTask(this).start();
            if (debugEnabled) {
                MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Started DailyResetTask for Topluck.");
            }
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public void reloadPlugin() {
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Reloading Argus configuration...");
        
        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Reloading configuration...");
        }

        ARGUS.debugEnabled = this.getConfig().getBoolean("debug");
        ARGUS.spectateEnabled = this.getConfig().getBoolean("commands.spectate.enabled", true);
        ARGUS.topluckEnabled = this.getConfig().getBoolean("commands.topluck.enabled",true );
        ARGUS.topluckDataResetInterval = this.getConfig().getInt("commands.topluck.player-data-reset-interval", 864000); // Reload configuration

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Debug mode is " + (debugEnabled ? "enabled" : "disabled"));
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Spectate command enabled: " + spectateEnabled);
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Topluck command enabled: " + topluckEnabled);
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Topluck data reset interval: " + topluckDataResetInterval);
        }
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Custom configuration loaded.");
        }
    }

    @Override
    public void onDisable() {
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Plugin disabled.");
        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "ARGUS plugin has been disabled.");
        }
    }
}
