package eu.sunrisenetwork.argusplugin.listeners;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    public static Map<Inventory, Player> inventories = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventories.containsKey(inventory)) {
            Player target = inventories.get(inventory);
            
            // Synchronize the changes immediately after the click
            syncInventories(inventory, target);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventories.containsKey(inventory)) {
            Player target = inventories.get(inventory);
            
            // Synchronize one last time when closing the inventory
            syncInventories(inventory, target);
            
            // Remove the inventory from the map
            inventories.remove(inventory);
        }
    }

    private void syncInventories(Inventory customInventory, Player target) {
        // Synchronize the main inventory slots
        for (int i = 0; i < 36; i++) {
            target.getInventory().setItem(i, customInventory.getItem(i));
        }

        // Synchronize the armor slots
        ItemStack[] armorContents = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armorContents[i] = customInventory.getItem(36 + i);
        }
        target.getInventory().setArmorContents(armorContents);

        // Synchronize the off-hand slot
        target.getInventory().setItemInOffHand(customInventory.getItem(40));

        // Update the player's inventory
        target.updateInventory();
    }
}