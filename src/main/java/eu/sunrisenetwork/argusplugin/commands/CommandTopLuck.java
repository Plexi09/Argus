package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import eu.sunrisenetwork.argusplugin.data.LuckCalculator;
import eu.sunrisenetwork.argusplugin.util.InventoryManager;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;

public class CommandTopLuck implements CommandExecutor {
    private final JavaPlugin plugin;
    
    public CommandTopLuck(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessageUtils.sendDebugMessage(sender, "Executing /topluck command.");

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            MessageUtils.sendDebugMessage(plugin.getServer().getConsoleSender(),("Command sender is not a player."));
            return true;
        }
        
        Player player = (Player) sender;
        MessageUtils.sendDebugMessage(sender, "Command sender is player: " + player.getName());
        
        if (args.length > 0 && args[0].equalsIgnoreCase("reset")) {
        	MessageUtils.sendDebugMessage(plugin.getServer().getConsoleSender(), ("Reset argument detected."));
            if (!player.hasPermission("argus.topluck.reset")) {
                player.sendMessage("You do not have permission to use this command.");
                MessageUtils.sendDebugMessage(plugin.getServer().getConsoleSender(), ("Player lacks permission to reset stats."));
                return true;
            }
            
            LuckCalculator.resetStats();
            player.sendMessage("Player stats reset successfully.");
            MessageUtils.sendDebugMessage(plugin.getServer().getConsoleSender(), ( "Player stats reset successfully."));
            return true;
        }
        
        Inventory topLuckInventory = InventoryManager.createTopLuckInventory();
        player.openInventory(topLuckInventory);
        MessageUtils.sendDebugMessage(plugin.getServer().getConsoleSender(), ("Opened Top Luck inventory for player: " + player.getName()));
        
        return true;
    }
}
