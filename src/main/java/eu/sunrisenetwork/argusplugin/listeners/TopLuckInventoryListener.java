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
import eu.sunrisenetwork.argusplugin.util.MessageUtils;

public class TopLuckInventoryListener implements Listener {
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Inventory click detected: " + event.getView().getTitle());

        if (event.getView().getTitle().equals("Top Luck") || event.getView().getTitle().startsWith("Actions pour ")) {
            event.setCancelled(true);
            MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Inventory interaction cancelled.");

            if (event.getView().getTitle().equals("Top Luck")) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                    SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                    Player target = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                    
                    if (target != null) {
                        Player clicker = (Player) event.getWhoClicked();
                        clicker.openInventory(InventoryManager.createPlayerActionsInventory(target));
                        MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Opened actions inventory for player: " + target.getName());
                    }
                }
            } else if (event.getView().getTitle().startsWith("Actions pour ")) {
                Player clicker = (Player) event.getWhoClicked();
                String targetName = event.getView().getTitle().substring("Actions pour ".length());
                Player target = Bukkit.getPlayer(targetName);
                
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && target != null) {
                    MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Action item clicked: " + clickedItem.getType());

                    switch (clickedItem.getType()) {
                        case CHEST:
                            clicker.performCommand("openinv " + target.getName());
                            clicker.closeInventory();
                            MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Executed /openinv command for player: " + target.getName());
                            break;
                        case ENDER_PEARL:
                            clicker.performCommand("spectate " + target.getName());
                            clicker.closeInventory();
                            MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Executed /spectate command for player: " + target.getName());
                            break;
                        case ICE:
                            clicker.performCommand("freeze " + target.getName());
                            clicker.closeInventory();
                            MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Executed /freeze command for player: " + target.getName());
                            break;
                        case BARRIER:
                            clicker.openInventory(InventoryManager.createTopLuckInventory());
                            MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Returned to Top Luck inventory.");
                            break;
                        default:
                            MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Unknown item type clicked.");
                            break;
                    }
                }
            }
        }
    }
}
