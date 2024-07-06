package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;
import eu.sunrisenetwork.argusplugin.listeners.InventoryClickListener;

public class CommandOpeninv implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }
        
        // Cast the sender to Player
        Player player = (Player) sender;
        
        // Check if the command has exactly one argument (player name)
        if (args.length != 1) {
            MessageUtils.sendMessage(sender, "Usage: /openinv <player>");
            return true;
        }
        
        // Get the target player by name
        Player target = Bukkit.getPlayer(args[0]);
        
        // Check if the target player is online
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(player, "Player not found or not online.");
            return true;
        }
        
        // Create a custom inventory with a size of 45 slots (36 for inventory + 4 for armor + 1 for off-hand + 4 extra)
        Inventory customInventory = Bukkit.createInventory(null, 45, target.getName() + "'s Inventory");
        
        // Copy the target player's inventory to the custom inventory
        ItemStack[] targetContents = target.getInventory().getContents();
        for (int i = 0; i < targetContents.length; i++) {
            customInventory.setItem(i, targetContents[i]);
        }
        
        // Copy the target player's armor to the custom inventory (slots 36 to 39)
        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            customInventory.setItem(36 + i, armorContents[i]);
        }
        
        // Copy the target player's off-hand item to the custom inventory (slot 40)
        customInventory.setItem(40, target.getInventory().getItemInOffHand());
        
        // Add the inventory and target player to the map in InventoryClickListener
        InventoryClickListener.inventories.put(customInventory, target);
        
        // Open the custom inventory for the player
        player.openInventory(customInventory);
        
        MessageUtils.sendMessage(player, "Opened inventory of " + target.getName());
        
        return true;
    }
}