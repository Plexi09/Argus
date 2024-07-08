package eu.sunrisenetwork.argusplugin.commands;

import eu.sunrisenetwork.argusplugin.ARGUS;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStaffChat implements CommandExecutor {
    private final ARGUS plugin;

    public CommandStaffChat(ARGUS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /**if (!(sender instanceof Player)) {
        *    MessageUtils.sendMessage(sender, "Only players can use this command.");
        *    return true;
        *}
		*/
        Player player = (Player) sender;

        if (args.length == 0) {
            MessageUtils.sendMessage(player, "Usage: /sc <message>");
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        String finalMessage = message.toString().trim();

        for (Player onlineStaff : plugin.getServer().getOnlinePlayers()) {
            if (onlineStaff.hasPermission("argus.staffchat.read")) {
                MessageUtils.sendStaffMessage(onlineStaff, player.getName(), finalMessage);
            }
        }

        return true;
    }
}
