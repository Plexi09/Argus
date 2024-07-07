package eu.sunrisenetwork.argusplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import eu.sunrisenetwork.argusplugin.util.MessageUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LuckCalculator {
    private static Map<UUID, Integer> dailyDiamondsMined = new HashMap<>();
    private static Map<UUID, Integer> lastStoneMined = new HashMap<>();
    private static Map<UUID, Integer> lastDiamondMined = new HashMap<>();
    private static Map<UUID, Integer> lastDeepslateDiamondMined = new HashMap<>();

    public static Map<Player, Double> calculateLuckPercentages() {
        Map<Player, Double> luckPercentages = new HashMap<>();
        MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Calculating luck percentages...");

        for (Player player : Bukkit.getOnlinePlayers()) {
            int stoneMined = getStatistic(player, Statistic.MINE_BLOCK, Material.STONE) - lastStoneMined.getOrDefault(player.getUniqueId(), 0);
            int diamondMined = getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE) - lastDiamondMined.getOrDefault(player.getUniqueId(), 0);
            int deepslateDiamondMined = getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE) - lastDeepslateDiamondMined.getOrDefault(player.getUniqueId(), 0);

            int totalDiamondsMined = diamondMined + deepslateDiamondMined;
            double luckPercentage = (stoneMined + totalDiamondsMined == 0) ? 0 : (double) totalDiamondsMined / (stoneMined + totalDiamondsMined) * 100;
            luckPercentages.put(player, luckPercentage);

            MessageUtils.sendDebugMessage(player, "Player: " + player.getName() + ", Stone mined: " + stoneMined + ", Diamonds mined: " + totalDiamondsMined + ", Luck percentage: " + luckPercentage);
        }

        return luckPercentages;
    }

    public static int getDiamondsMined(Player player) {
        int diamonds = dailyDiamondsMined.getOrDefault(player.getUniqueId(), 0);
        MessageUtils.sendDebugMessage(player, "Diamonds mined by player " + player.getName() + ": " + diamonds);
        return diamonds;
    }

    public static int getLastStoneMined(Player player) {
        int stones = lastStoneMined.getOrDefault(player.getUniqueId(), 0);
        MessageUtils.sendDebugMessage(player, "Last stone mined by player " + player.getName() + ": " + stones);
        return stones;
    }

    public static int getLastDiamondMined(Player player) {
        int diamonds = lastDiamondMined.getOrDefault(player.getUniqueId(), 0);
        MessageUtils.sendDebugMessage(player, "Last diamond mined by player " + player.getName() + ": " + diamonds);
        return diamonds;
    }

    public static int getLastDeepslateDiamondMined(Player player) {
        int deepslateDiamonds = lastDeepslateDiamondMined.getOrDefault(player.getUniqueId(), 0);
        MessageUtils.sendDebugMessage(player, "Last deepslate diamond mined by player " + player.getName() + ": " + deepslateDiamonds);
        return deepslateDiamonds;
    }

    public static void resetStats() {
        MessageUtils.sendDebugMessage(Bukkit.getConsoleSender(), "Resetting all player stats...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            lastStoneMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.STONE));
            lastDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
            lastDeepslateDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE));
            dailyDiamondsMined.put(uuid, 0);

            MessageUtils.sendDebugMessage(player, "Reset stats for player: " + player.getName());
        }
    }

    public static void resetPlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        MessageUtils.sendDebugMessage(player, "Resetting stats for player: " + player.getName());
        lastStoneMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.STONE));
        lastDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
        lastDeepslateDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE));
        dailyDiamondsMined.put(uuid, 0);
    }

    private static int getStatistic(Player player, Statistic statistic, Material material) {
        int stat = player.getStatistic(statistic, material);
        MessageUtils.sendDebugMessage(player, "Statistic for " + material.name() + ": " + stat);
        return stat;
    }
}
