package eu.sunrisenetwork.argusplugin;

import eu.sunrisenetwork.argusplugin.commands.*;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;
import eu.sunrisenetwork.argusplugin.listeners.TopLuckInventoryListener;
import eu.sunrisenetwork.argusplugin.listeners.InventoryClickListener;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ARGUS extends JavaPlugin {
    private File customConfigFile;
    private FileConfiguration customConfig;
    private boolean debug;

    @Override
    public void onEnable() {
        long startTime = System.nanoTime();  // Start time

        MessageUtils.sendMessage(getServer().getConsoleSender(), "Starting Argus...");
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Registering commands... ");

        this.getCommand("spectate").setExecutor(new CommandSpectate());

        this.getCommand("openinv").setExecutor(new CommandOpeninv());
        this.getCommand("oi").setExecutor(new CommandOpeninv());
        this.getCommand("inv").setExecutor(new CommandOpeninv());
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

        getCommand("arrest").setExecutor(new CommandArrest(this));

        this.getCommand("topluck").setExecutor(new CommandTopLuck());

        MessageUtils.sendMessage(getServer().getConsoleSender(), "Loading listeners... ");
        getServer().getPluginManager().registerEvents(new TopLuckInventoryListener(), this);

        MessageUtils.sendMessage(getServer().getConsoleSender(), "Loading the config... ");
        createCustomConfig();
        Boolean time = ARGUS.getConfig().getBoolean("debug");	

        if (debug) {
        	MessageUtils.sendMessage(getServer().getConsoleSender(),"Debug mode is enabled.");
        }

        long endTime = System.nanoTime();  // End time
        long duration = (endTime - startTime) / 1000000;  // Duration in milliseconds

        MessageUtils.sendMessage(getServer().getConsoleSender(), "Plugin started in " + duration + " ms.");
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Thanks you for using Argus !");
    }
    
    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "custom.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("custom.yml", false);
         }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            ((Throwable) e).printStackTrace();
        }
        /* User Edit:
            Instead of the above Try/Catch, you can also use
            YamlConfiguration.loadConfiguration(customConfigFile)
        */
    }

    @Override
    public void onDisable() {
        MessageUtils.sendMessage(getServer().getConsoleSender(), "Plugin disabled.");
    }
}
