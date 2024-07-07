package eu.sunrisenetwork.argusplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import eu.sunrisenetwork.argusplugin.util.TopluckInventoryManager;

public class TopLuckInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Top Luck") || event.getView().getTitle().startsWith("Actions for ")) {
            event.setCancelled(true);

            Player clicker = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null) {
                if (event.getView().getTitle().equals("Top Luck")) {
                    if (clickedItem.getType() == Material.PLAYER_HEAD) {
                        SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                        Player target = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());

                        if (target != null) {
                            clicker.openInventory(TopluckInventoryManager.createPlayerActionsInventory(target));
                        }
                    }
                } else if (event.getView().getTitle().startsWith("Actions for ")) {
                    String targetName = event.getView().getTitle().substring("Actions for ".length());
                    Player target = Bukkit.getPlayer(targetName);

                    if (target != null) {
                        switch (clickedItem.getType()) {
                            case CHEST:
                                clicker.closeInventory();
                                clicker.performCommand("openinv " + target.getName());
                                break;
                            case ENDER_PEARL:
                                clicker.closeInventory();
                                clicker.performCommand("spectate " + target.getName());
                                break;
                            case ICE:
                                clicker.closeInventory();
                                clicker.performCommand("freeze " + target.getName());
                                break;
                            case BARRIER:
                                clicker.openInventory(TopluckInventoryManager.createTopLuckInventory());
                                break;
                            case ANVIL:
                            	clicker.performCommand("arrest " + target.getName());
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
}
