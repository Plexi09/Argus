package eu.sunrisenetwork.argusplugin.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtils {
    private static final String PREFIX = ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.RED + "ARGUS" + ChatColor.GRAY + "" + ChatColor.BOLD + "] " + ChatColor.RESET; //[ARGUS]

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + ChatColor.GRAY + message);
    }
    
    public static void sendDebugMessage(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + ChatColor.YELLOW + "DEBUG: " + ChatColor.GRAY + message);
    }
    
    public static void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + ChatColor.RED + "ERROR: " + ChatColor.GRAY + message);
    }
    
    public static void sendStaffMessage(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + ChatColor.BLUE + "STAFF CHAT: " + ChatColor.GRAY + sender + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message);
    }
}
