package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.sunrisenetwork.argusplugin.ARGUS;
import eu.sunrisenetwork.argusplugin.listeners.InventoryClickListener;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandOpeninv implements CommandExecutor {
    private final JavaPlugin plugin;

    public CommandOpeninv(JavaPlugin plugin) {
        this.plugin = plugin;
    }

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
            if (ARGUS.debugEnabled) {
                MessageUtils.sendMessage(plugin.getServer().getConsoleSender(), sender + " failed to use command /openinv: Incorrect usage");
            }
            return true;
        }
        
        // Get the target player by name
        Player target = Bukkit.getPlayer(args[0]);
        
        // Check if the target player is online
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(player, args[0] + " is not online.");
            return true;
        }
        
        // Create a custom inventory with a size of 54 slots (36 for inventory + 4 for armor + 1 for off-hand + 13 extra)
        Inventory customInventory = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");
        
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
        
        // Add glass panes between stats and control buttons (slots 48 to 50)
        for (int i = 48; i <= 50; i++) {
            customInventory.setItem(i, createGlassPane());
        }
        
        // Create the control buttons and add them to the last row
        customInventory.setItem(45, createStatsItem(Material.TOTEM_OF_UNDYING, "§cHealth: " + target.getHealth()));
        customInventory.setItem(46, createStatsItem(Material.COOKED_BEEF, "§eFood: " + target.getFoodLevel()));
        customInventory.setItem(47, createStatsItem(Material.EXPERIENCE_BOTTLE, "§aExp: " + "lvl " + target.getLevel(), "§aPoints: " + target.getExp()));
        
        customInventory.setItem(51, createControlItem(Material.STRUCTURE_VOID, "§cClear Inventory", "§7Click to clear the player's inventory."));
        customInventory.setItem(52, createControlItem(Material.DISPENSER, "§eShuffle Inventory", "§7Click to shuffle the player's inventory."));
        customInventory.setItem(53, createControlItem(Material.ARROW, "§aRefresh", "§7Click to refresh the inventory view."));
        
        
        // Add the inventory and target player to the map in InventoryClickListener
        InventoryClickListener.inventories.put(customInventory, target);
        
        // Open the custom inventory for the player
        player.openInventory(customInventory);
        
        MessageUtils.sendMessage(player, "Opened inventory of " + target.getName() + ".");
        
        return true;
    }
    
    private ItemStack createGlassPane() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createControlItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        List<String> loreList = new ArrayList<>();
        for (String line : lore) {
            loreList.add(line);
        }
        meta.setLore(loreList);
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createStatsItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        List<String> loreList = new ArrayList<>();
        for (String line : lore) {
            loreList.add(line);
        }
        meta.setLore(loreList);
        
        item.setItemMeta(meta);
        return item;
    }
}
