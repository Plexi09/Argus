package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

public class CommandOpenender implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
        	MessageUtils.sendMessage(player, "Usage : /openender <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
        	MessageUtils.sendMessage(player, "Joueur introuvable.");
            return true;
        }

        player.openInventory(target.getEnderChest());
        return true;
    }
}