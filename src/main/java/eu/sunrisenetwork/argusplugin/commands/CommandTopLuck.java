package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import eu.sunrisenetwork.argusplugin.util.InventoryManager;

public class CommandTopLuck implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        Inventory topLuckInventory = InventoryManager.createTopLuckInventory();
        player.openInventory(topLuckInventory);
        
        return true;
    }
}