package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

public class CommandSilentcontainer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

//        Player player = (Player) sender;

        // TODO: Add the logic
        MessageUtils.sendMessage(sender, "This command is not yet fonctional.");
		return true;
    }
}