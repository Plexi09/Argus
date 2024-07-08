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

        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("argus.staffchat.speak")) {
            MessageUtils.sendMessage(player, "You don't have permission to speak in the staff chat.");
            return true;
        }

        if (args.length == 0) {
            MessageUtils.sendMessage(player, "Usage: /sc <message>");
            return true;
        }

        // Construire le message à envoyer dans le staff chat
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        // Envoyer le message aux joueurs ayant la permission de lire le staff chat
        for (Player onlineStaff : plugin.getServer().getOnlinePlayers()) {
            if (onlineStaff.hasPermission("argus.staffchat.read")) {
                MessageUtils.sendStaffMessage(onlineStaff, message.toString());
            }
        }

        return true;
    }
}
