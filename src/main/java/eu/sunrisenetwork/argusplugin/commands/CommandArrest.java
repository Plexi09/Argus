package eu.sunrisenetwork.argusplugin.commands;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandArrest implements CommandExecutor, Listener {

    private final Map<UUID, Player> arrestedPlayers;

    public CommandArrest(Plugin plugin) {
        this.arrestedPlayers = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	MessageUtils.sendMessage(sender, "Only players can use this command.");
            return true;
        }

        Player staff = (Player) sender;
        
        if (args.length < 2) {
        	MessageUtils.sendMessage(staff, "Usage : /arrest <player> <reason>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
        	MessageUtils.sendMessage(staff, args[0] + " is not online.");
            return true;
        }

        String reason = String.join(" ", args).substring(args[0].length() + 1);

        showArrestInterface(staff, target, reason);
        playArrestSound(target);
        return true;
    }

    private void showArrestInterface(Player staff, Player target, String reason) {
        Inventory arrestInventory = Bukkit.createInventory(new ArrestInventoryHolder(target.getUniqueId(), staff), 9, ChatColor.RED + "You got arrested !");
        MessageUtils.sendMessage(target, "You got arrested !");
        
        ItemStack reasonItem = createReasonItem(staff, reason);
        arrestInventory.setItem(4, reasonItem);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = reasonItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "");
        
        
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                arrestInventory.setItem(i, barrier);
            }
        }

        target.openInventory(arrestInventory);
        arrestedPlayers.put(target.getUniqueId(), staff);
    }



    private ItemStack createReasonItem(Player staff, String reason) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Arrested by: " + staff.getName());
        meta.setLore(Collections.singletonList(ChatColor.GRAY + reason));
        item.setItemMeta(meta);

        return item;
    }


    private void playArrestSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100.0f, 0.5f);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof ArrestInventoryHolder) {
            ArrestInventoryHolder holder = (ArrestInventoryHolder) inventory.getHolder();
            Player target = Bukkit.getPlayer(holder.getTargetUniqueId());
            if (target != null && arrestedPlayers.containsKey(target.getUniqueId())) {
                Player staff = arrestedPlayers.get(target.getUniqueId());
                MessageUtils.sendMessage(staff, ChatColor.YELLOW + target.getName() + " has closed the arrest interface.");
                arrestedPlayers.remove(target.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && clickedInventory.getHolder() instanceof ArrestInventoryHolder) {
            event.setCancelled(true);
        }
    }

    private static class ArrestInventoryHolder implements InventoryHolder {
        private final UUID targetUniqueId;

        public ArrestInventoryHolder(UUID targetUniqueId, Player staff) {
            this.targetUniqueId = targetUniqueId;
        }

        public UUID getTargetUniqueId() {
            return targetUniqueId;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
