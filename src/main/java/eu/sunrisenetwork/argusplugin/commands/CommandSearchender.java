package eu.sunrisenetwork.argusplugin.commands;

//import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;

public class CommandSearchender implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

//      Player player = (Player) sender;
        
        MessageUtils.sendMessage(sender, "This command is not yet fonctional.");
		return true;

//        if (args.length < 1) {
//        	MessageUtils.sendMessage(sender, "Utilisation : /searchender <item> [minAmount]");
//            return true;
//        }
//
//        String itemName = args[0];
//        int minAmount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
//
//        List<String> playersWithItem = new ArrayList<>();
//
//        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//            ItemStack[] enderContents = onlinePlayer.getEnderChest().getContents();
//            if (hasItemInEnderChest(enderContents, itemName, minAmount)) {
//                playersWithItem.add(onlinePlayer.getName());
//            }
//        }
//
//      MessageUtils.sendMessage(player, "Joueurs ayant " + itemName + " dans leur coffre de fin :");
//      MessageUtils.sendMessage(player, String.join(", ", playersWithItem));
//      return true;
//  }
        

//  private boolean hasItemInEnderChest(ItemStack[] contents, String itemName, int minAmount) {
//      return Arrays.stream(contents)
//              .filter(item -> item != null && item.getType().toString().equalsIgnoreCase(itemName))
//              .mapToInt(ItemStack::getAmount)
//              .sum() >= minAmount;
  }
}