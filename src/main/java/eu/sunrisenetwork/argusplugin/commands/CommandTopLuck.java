package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import eu.sunrisenetwork.argusplugin.data.LuckCalculator;
import eu.sunrisenetwork.argusplugin.util.InventoryManager;

public class CommandTopLuck implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length > 0 && args[0].equalsIgnoreCase("reset")) {
            if (!player.hasPermission("argus.topluck.reset")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }
            
            LuckCalculator.resetStats();
            player.sendMessage("Player stats reset successfully.");
            return true;
        }
        
        Inventory topLuckInventory = InventoryManager.createTopLuckInventory();
        player.openInventory(topLuckInventory);
        
        return true;
    }
}
