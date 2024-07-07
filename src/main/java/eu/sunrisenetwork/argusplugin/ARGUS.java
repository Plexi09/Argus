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
        
        createCustomConfig();
        ARGUS.debugEnabled = this.getConfig().getBoolean("debug");
        ARGUS.spectateEnabled = this.getConfig().getBoolean("commands.spectate.enabled");
        ARGUS.topluckEnabled = this.getConfig().getBoolean("commands.topluck.enabled");
        ARGUS.topluckDataResetInterval = this.getConfig().getInt("commands.topluck.player-data-reset-interval", 864000); // Default to 864000 ticks

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Debug mode is enabled.");
        }

        if (debugEnabled) {
            MessageUtils.sendDebugMessage(getServer().getConsoleSender(), "Registering commands...");
        }

        if (spectateEnabled) {
            this.getCommand("spectate").setExecutor(new CommandSpectate(this));
        }

        if (topluckEnabled) {
            this.getCommand("topluck").setExecutor(new CommandTopLuck());
        }

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
        this.getCommand("sc").setExecutor(new CommandSilentcontainer());
        this.getCommand("silentc").setExecutor(new CommandSilentcontainer());
        this.getCommand("silentchest").setExecutor(new CommandSilentcontainer());

        this.getCommand("arrest").setExecutor(new CommandArrest(this));
        this.getCommand("argusreload").setExecutor(new Reload(this));

        getServer().getPluginManager().registerEvents(new TopLuckInventoryListener(), this);

        long endTime = System.nanoTime();  // End time
        long duration = (endTime - startTime) / 1000000;  // Duration in milliseconds

        if (debugEnabled) {
            MessageUtils.sendMessage(getServer().getConsoleSender(), "Finished loading in " + duration + " ms !");
        } else {
            MessageUtils.sendMessage(getServer().getConsoleSender(), "Finished loading !");
        }
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Thanks you for using Argus !");

        // Start the daily reset task if topluck is enabled
        if (topluckEnabled) {
            new DailyResetTask(this).start();
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public void reloadPlugin() {
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Reloading Argus configuration...");
        ARGUS.debugEnabled = this.getConfig().getBoolean("debug");
        ARGUS.spectateEnabled = this.getConfig().getBoolean("commands.spectate.enabled");
        ARGUS.topluckEnabled = this.getConfig().getBoolean("commands.topluck.enabled");
        ARGUS.topluckDataResetInterval = this.getConfig().getInt("commands.topluck.player-data-reset-interval", 864000); // Reload configuration
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
    }

    @Override
    public void onDisable() {
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Plugin disabled.");
    }
}
