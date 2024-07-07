package eu.sunrisenetwork.argusplugin.listeners;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class InventoryClickListener implements Listener {
    public static Map<Inventory, Player> inventories = new HashMap<>();
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player clicker = (Player) event.getWhoClicked();
        
        if (inventories.containsKey(inventory)) {
            Player target = inventories.get(inventory);
            ItemStack currentItem = event.getCurrentItem();
            
            if (currentItem != null) {
                Material material = currentItem.getType();
                int slot = event.getSlot();
                
                // Annuler l'événement pour les items de stats et de contrôle
                if ((slot == 45 || slot == 46) || (slot >= 51 && slot <= 53)) {
                    event.setCancelled(true);
                }
                
                // Gérer les actions des boutons de contrôle
                if (slot >= 51 && slot <= 53) {
                    switch (material) {
                        case STRUCTURE_VOID:
                            target.getInventory().clear();
                            MessageUtils.sendMessage(clicker, "Cleared the inventory of " + target.getName());
                            clicker.closeInventory();
                            clicker.openInventory(createCustomInventory(target));
                            break;
                        case DISPENSER:
                            shuffleInventory(target);
                            MessageUtils.sendMessage(clicker, "Shuffled the inventory of " + target.getName());
                            clicker.closeInventory();
                            clicker.openInventory(createCustomInventory(target));
                            break;
                        case ARROW:
                            MessageUtils.sendMessage(clicker, "Refreshed the inventory view.");
                            clicker.closeInventory();
                            clicker.openInventory(createCustomInventory(target));
                            break;
                        default:
                            return;
                    }
                }
            }
        }
    }
    
    private void shuffleInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            int randomIndex = (int) (Math.random() * contents.length);
            ItemStack temp = contents[i];
            contents[i] = contents[randomIndex];
            contents[randomIndex] = temp;
        }
        player.getInventory().setContents(contents);
    }
    
    private Inventory createCustomInventory(Player target) {
        Inventory customInventory = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");
        
        ItemStack[] targetContents = target.getInventory().getContents();
        for (int i = 0; i < targetContents.length; i++) {
            customInventory.setItem(i, targetContents[i]);
        }
        
        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            customInventory.setItem(45 + i, armorContents[i]);
        }
        
        // Ajouter les items de stats aux emplacements 45 et 46
        customInventory.setItem(45, createStatsItem(Material.COOKED_BEEF, "§aFood: " + target.getFoodLevel()));
        customInventory.setItem(46, createStatsItem(Material.TOTEM_OF_UNDYING, "§cHealth: " + target.getHealth()));
        
        // Ajouter les boutons de contrôle aux emplacements 51 à 53
        customInventory.setItem(51, createControlItem(Material.STRUCTURE_VOID, "§cClear Inventory", "§7Click to clear the player's inventory."));
        customInventory.setItem(52, createControlItem(Material.DISPENSER, "§eShuffle Inventory", "§7Click to shuffle the player's inventory."));
        customInventory.setItem(53, createControlItem(Material.ARROW, "§aRefresh", "§7Click to refresh the inventory view."));
        
        inventories.put(customInventory, target);
        return customInventory;
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
}
