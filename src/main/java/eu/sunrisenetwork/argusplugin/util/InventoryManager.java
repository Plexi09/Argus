package eu.sunrisenetwork.argusplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class InventoryManager {
    
    public static Inventory createTopLuckInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Top Luck");
        
        Map<Player, Double> luckPercentages = LuckCalculator.calculateLuckPercentages();
        List<Map.Entry<Player, Double>> sortedEntries = new ArrayList<>(luckPercentages.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        int slot = 0;
        for (Map.Entry<Player, Double> entry : sortedEntries) {
            if (slot >= 54) break;
            
            Player player = entry.getKey();
            double luckPercentage = entry.getValue();
            
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(player);
            meta.setDisplayName("§6" + player.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Chance: §a" + String.format("%.2f", luckPercentage) + "%");
            meta.setLore(lore);
            
            skull.setItemMeta(meta);
            inventory.setItem(slot++, skull);
        }
        
        return inventory;
    }
    
    public static Inventory createPlayerActionsInventory(Player target) {
        Inventory inventory = Bukkit.createInventory(null, 36, "Actions pour " + target.getName());

        ItemStack openInv = createItem(Material.CHEST, "§6Open inventory", "§7Click to open" + target.getName() + "'s inventory.");
        ItemStack spectate = createItem(Material.ENDER_PEARL, "§6Spectate", "§7Click to spectate " + target.getName());
        ItemStack freeze = createItem(Material.ICE, "§6Freeze", "§7Click to freeze " + target.getName());

        int diamondsMined = LuckCalculator.getDiamondsMined(target);
        Map<Player, Double> luckPercentages = LuckCalculator.calculateLuckPercentages();
        double luckPercentage = luckPercentages.getOrDefault(target, 0.0);
        ItemStack diamonds = createItem(Material.DIAMOND_ORE, "§bDiamants minés: " + diamondsMined, "§7Chance: §a" + String.format("%.2f", luckPercentage) + "%");

        ItemStack back = createItem(Material.BARRIER, "§cRetour", "§7Clic pour revenir au Top Luck");

        inventory.setItem(11, openInv);
        inventory.setItem(13, spectate);
        inventory.setItem(15, freeze);
        inventory.setItem(30, diamonds);
        inventory.setItem(32, back);

        return inventory;
    }

    
    private static ItemStack createItem(Material material, String name, String... lore) {
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