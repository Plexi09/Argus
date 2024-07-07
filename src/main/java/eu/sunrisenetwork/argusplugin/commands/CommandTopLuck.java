package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import eu.sunrisenetwork.argusplugin.data.LuckCalculator;
import eu.sunrisenetwork.argusplugin.util.TopluckInventoryManager;
import eu.sunrisenetwork.argusplugin.util.MessageUtils;

public class CommandTopLuck implements CommandExecutor {
    private final JavaPlugin plugin;
    
    public CommandTopLuck(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length > 0 && args[0].equalsIgnoreCase("reset")) {
            
            LuckCalculator.resetStats();
            MessageUtils.sendMessage(player, "Player stats reset successfully.");
            MessageUtils.sendMessage(plugin.getServer().getConsoleSender(), ( "Topluck: Player stats reset successfully."));
            return true;
        }
        
        Inventory topLuckInventory = TopluckInventoryManager.createTopLuckInventory();
        player.openInventory(topLuckInventory);
        
        return true;
    }
}
