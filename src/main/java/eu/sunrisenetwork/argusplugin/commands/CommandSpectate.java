package eu.sunrisenetwork.argusplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;
import eu.sunrisenetwork.argusplugin.util.PlayerState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandSpectate implements CommandExecutor {
    private final Map<UUID, PlayerState> playerStates = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
        if (!(sender instanceof Player)) {
        	MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (args.length != 1) {
            MessageUtils.sendMessage(player, "Usage: /spectate <player>|off");
            return false;
        }

        if (args[0].equalsIgnoreCase("off")) {
            // Restore previous state
            if (playerStates.containsKey(playerId)) {
                PlayerState state = playerStates.remove(playerId);
                player.setGameMode(state.getGameMode());
                player.teleport(state.getLocation());
                MessageUtils.sendMessage(player, "You have been restored to your previous game mode and location.");
            } else {
                MessageUtils.sendMessage(player, "No previous state found.");
            }
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtils.sendMessage(player, args[0] + " is not online.");
            return false;
        }
        
        if (target == player) {
            MessageUtils.sendMessage(player, "You cannot spectate yourself!");
            return false;
        }

        // Save current state and set to spectator mode
        playerStates.put(playerId, new PlayerState(player.getGameMode(), player.getLocation()));
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target);
        MessageUtils.sendMessage(player, "You are now spectating " + target.getName() + ".");
        return true;
    }

}
