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
        MessageUtils.sendDebugMessage(event.getWhoClicked(), "Inventory click detected: " + event.getView().getTitle());

        if (event.getView().getTitle().equals("Top Luck")) {
            event.setCancelled(true);
            MessageUtils.sendDebugMessage(event.getWhoClicked(), "Top Luck inventory interaction.");

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                Player target = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                
                if (target != null) {
                    Player clicker = (Player) event.getWhoClicked();
                    clicker.openInventory(InventoryManager.createPlayerActionsInventory(target));
                    MessageUtils.sendDebugMessage(clicker, "Opened actions inventory for player: " + target.getName());
                }
            }
        } else if (event.getView().getTitle().startsWith("Actions pour ")) {
            event.setCancelled(true);
            MessageUtils.sendDebugMessage(event.getWhoClicked(), "Actions pour inventory interaction.");

            Player clicker = (Player) event.getWhoClicked();
            String targetName = event.getView().getTitle().substring("Actions pour ".length());
            Player target = Bukkit.getPlayer(targetName);
            
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && target != null) {
                MessageUtils.sendDebugMessage(clicker, "Action item clicked: " + clickedItem.getType());

                switch (clickedItem.getType()) {
                    case CHEST:
                        clicker.performCommand("openinv " + target.getName());
                        clicker.closeInventory();
                        MessageUtils.sendDebugMessage(clicker, "Executed /openinv command for player: " + target.getName());
                        break;
                    case ENDER_PEARL:
                        clicker.performCommand("spectate " + target.getName());
                        clicker.closeInventory();
                        MessageUtils.sendDebugMessage(clicker, "Executed /spectate command for player: " + target.getName());
                        break;
                    case ICE:
                        clicker.performCommand("freeze " + target.getName());
                        clicker.closeInventory();
                        MessageUtils.sendDebugMessage(clicker, "Executed /freeze command for player: " + target.getName());
                        break;
                    case BARRIER:
                        clicker.openInventory(InventoryManager.createTopLuckInventory());
                        MessageUtils.sendDebugMessage(clicker, "Returned to Top Luck inventory.");
                        break;
                    default:
                        MessageUtils.sendDebugMessage(clicker, "Unknown item type clicked.");
                        break;
                }
            }
        }
    }
}
