package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandSearchinv implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            MessageUtils.sendMessage(player, "Usage : /searchinv <item> [minAmount]");
            return true;
        }

        String itemName = args[0];
        Material material = Material.matchMaterial(itemName);
        if (material == null) {
            MessageUtils.sendMessage(player, "The item " + itemName + " is not a valid item.");
            return true;
        }

        int minAmount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        
        if (minAmount < 1) {
        	MessageUtils.sendMessage(sender, "Invalid input. Usage : /searchinv <item> [minAmount]");
        }

        List<String> playersWithItem = new ArrayList<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = onlinePlayer.getInventory();
            if (hasItem(inv, material, minAmount)) {
                playersWithItem.add(onlinePlayer.getName());
            }
        }

        if (playersWithItem.isEmpty()) {
            MessageUtils.sendMessage(player, "There are no players with at least " + minAmount + " " + itemName + " in their inventory.");
        } if (minAmount > 1) {
            MessageUtils.sendMessage(player, "Players who have at least " + minAmount + " " + itemName + " in their inventory: " + String.join(", ", playersWithItem));
            //MessageUtils.sendMessage(player, String.join(", ", playersWithItem));
        }
        return true;
    }

    private boolean hasItem(PlayerInventory inv, Material material, int minAmount) {
        return Arrays.stream(inv.getContents())
                .filter(item -> item != null && item.getType() == material)
                .mapToInt(ItemStack::getAmount)
                .sum() >= minAmount;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String partialItem = args[0].toUpperCase();
            return Arrays.stream(Material.values())
                    .map(Material::toString)
                    .filter(item -> item.startsWith(partialItem))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
