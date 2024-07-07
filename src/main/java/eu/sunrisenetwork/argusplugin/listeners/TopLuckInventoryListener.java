package eu.sunrisenetwork.argusplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import eu.sunrisenetwork.argusplugin.util.InventoryManager;

public class TopLuckInventoryListener implements Listener {
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Top Luck")) {
            event.setCancelled(true);
            
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                Player target = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                
                if (target != null) {
                    Player clicker = (Player) event.getWhoClicked();
                    clicker.openInventory(InventoryManager.createPlayerActionsInventory(target));
                }
            }
        } else if (event.getView().getTitle().startsWith("Actions pour ")) {
            event.setCancelled(true);
            
            Player clicker = (Player) event.getWhoClicked();
            String targetName = event.getView().getTitle().substring("Actions pour ".length());
            Player target = Bukkit.getPlayer(targetName);
            
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && target != null) {
                switch (clickedItem.getType()) {
                    case CHEST:
                        clicker.performCommand("openinv " + target.getName());
                        clicker.closeInventory();
                        break;
                    case ENDER_PEARL:
                        clicker.performCommand("spectate " + target.getName());
                        clicker.closeInventory();
                        break;
                    case ICE:
                        clicker.performCommand("freeze " + target.getName());
                        clicker.closeInventory();
                        break;
                    case BARRIER:
                        clicker.openInventory(InventoryManager.createTopLuckInventory());
                        break;
				default:
					break;
                }
            }
        }
    }
}