package eu.sunrisenetwork.argusplugin.util;

import eu.sunrisenetwork.argusplugin.ARGUS;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Reload implements CommandExecutor {

    public Reload(JavaPlugin plugin) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    		MessageUtils.sendMessage(sender, "Reloading the config...");
            
            ARGUS argus = new ARGUS();
			argus.reloadPlugin();
            
            MessageUtils.sendMessage(sender, "Plugin reloaded successfully.");
            return true;
    }
}
