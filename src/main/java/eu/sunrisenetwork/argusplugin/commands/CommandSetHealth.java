package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

public class CommandSetHealth implements CommandExecutor {
	
    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
        	MessageUtils.sendMessage(sender, "Usage: /sethealth <player> <integer>");
            return false;
        }

        String targetName = args[0];
        int health;

        try {
        	health = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
        	MessageUtils.sendMessage(sender, "The second argument must be an integer.");
            return false;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(sender, targetName + " is not online.");
            return false;
        }

        if (health > target.getMaxHealth()) {
            MessageUtils.sendMessage(sender, "You tried to set the health of " + target.getName() + " to " + health + " but its max health is " + target.getMaxHealth() + ". Try running /setmaxhealth " + target.getName() + " " + health + " and run the command again.");
            return false;
        }

        target.setHealth(health);
        MessageUtils.sendMessage(sender, "Set the health of " + target.getName() + " to " + health + ".");
        return true;
    }
}
