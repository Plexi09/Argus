package eu.sunrisenetwork.argusplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class InventoryClickListener implements Listener {
    public static Map<Inventory, Player> inventories = new HashMap<>();
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        Player clicker = (Player) event.getWhoClicked();
        
        if (inventories.containsKey(topInventory)) {
            Player target = inventories.get(topInventory);
            ItemStack currentItem = event.getCurrentItem();
            
            // Annuler l'événement pour les items de stats et de contrôle
            if (event.getRawSlot() >= 45 && event.getRawSlot() <= 53) {
                event.setCancelled(true);
                
                // Gérer les actions des boutons de contrôle
                if (currentItem != null) {
                    Material material = currentItem.getType();
                    switch (material) {
                        case STRUCTURE_VOID:
                            clicker.closeInventory();
                        	clicker.performCommand("clear " + target.getName());
                            MessageUtils.sendMessage(clicker, "Cleared the inventory of " + target.getName());
                            clicker.openInventory(createCustomInventory(target));
                            break;
                        case DISPENSER:
                            shuffleInventory(target);
                            MessageUtils.sendMessage(clicker, "Shuffled the inventory of " + target.getName());
                            refreshInventory(clicker, target);
                            break;
                        case ARROW:
                            MessageUtils.sendMessage(clicker, "Refreshed the inventory view.");
                            refreshInventory(clicker, target);
                            break;
                        default:
                            return;
                    }
                }
            } else {
                // Synchroniser l'inventaire du joueur avec les changements de l'inventaire distant
                syncPlayerInventory(target, topInventory);
            }
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (inventories.containsKey(topInventory)) {
            Player target = inventories.get(topInventory);
            syncPlayerInventory(target, topInventory);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        if (inventories.containsKey(closedInventory)) {
            Player target = inventories.get(closedInventory);
            syncPlayerInventory(target, closedInventory);
            inventories.remove(closedInventory);
        }
    }

    private void syncPlayerInventory(Player player, Inventory customInventory) {
        ItemStack[] contents = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            contents[i] = customInventory.getItem(i);
        }
        player.getInventory().setContents(contents);
        
        ItemStack[] armorContents = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armorContents[i] = customInventory.getItem(36 + i);
        }
        player.getInventory().setArmorContents(armorContents);
        
        player.getInventory().setItemInOffHand(customInventory.getItem(40));
    }

    private void refreshInventory(Player clicker, Player target) {
        clicker.closeInventory();
        clicker.openInventory(createCustomInventory(target));
    }

    private void shuffleInventory(Player player) {
        List<ItemStack> contentsList = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                contentsList.add(item);
            }
        }
        Collections.shuffle(contentsList);
        
        ItemStack[] shuffledContents = new ItemStack[player.getInventory().getSize()];
        for (int i = 0; i < contentsList.size(); i++) {
            shuffledContents[i] = contentsList.get(i);
        }
        
        player.getInventory().setContents(shuffledContents);
    }

    private Inventory createCustomInventory(Player target) {
        Inventory customInventory = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");
        
        ItemStack[] targetContents = target.getInventory().getContents();
        for (int i = 0; i < targetContents.length; i++) {
            customInventory.setItem(i, targetContents[i]);
        }
        
        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            customInventory.setItem(36 + i, armorContents[i]);
        }
        
        customInventory.setItem(40, target.getInventory().getItemInOffHand());
        
        for (int i = 48; i <= 50; i++) {
            customInventory.setItem(i, createGlassPane());
        }
        
        customInventory.setItem(45, createStatsItem(Material.TOTEM_OF_UNDYING, "§cHealth: " + target.getHealth()));
        customInventory.setItem(46, createStatsItem(Material.COOKED_BEEF, "§eFood: " + target.getFoodLevel()));
        customInventory.setItem(47, createStatsItem(Material.EXPERIENCE_BOTTLE, "§aExp: " + "lvl " + target.getLevel(), "§aPoints: " + target.getExp()));
        
        customInventory.setItem(51, createControlItem(Material.STRUCTURE_VOID, "§cClear Inventory", "§7Click to clear the player's inventory."));
        customInventory.setItem(52, createControlItem(Material.DISPENSER, "§eShuffle Inventory", "§7Click to shuffle the player's inventory."));
        customInventory.setItem(53, createControlItem(Material.ARROW, "§aRefresh", "§7Click to refresh the inventory view."));
        
        inventories.put(customInventory, target);
        return customInventory;
    }
    
    private ItemStack createGlassPane() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
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
