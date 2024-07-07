package eu.sunrisenetwork.argusplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

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

        for (Player player : Bukkit.getOnlinePlayers()) {
            int stoneMined = getStatistic(player, Statistic.MINE_BLOCK, Material.STONE) - lastStoneMined.getOrDefault(player.getUniqueId(), 0);
            int diamondMined = getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE) - lastDiamondMined.getOrDefault(player.getUniqueId(), 0);
            int deepslateDiamondMined = getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE) - lastDeepslateDiamondMined.getOrDefault(player.getUniqueId(), 0);

            int totalDiamondsMined = diamondMined + deepslateDiamondMined;
            double luckPercentage = (stoneMined + totalDiamondsMined == 0) ? 0 : (double) totalDiamondsMined / (stoneMined + totalDiamondsMined) * 100;
            luckPercentages.put(player, luckPercentage);

        }

        return luckPercentages;
    }

    public static int getDiamondsMined(Player player) {
        return dailyDiamondsMined.getOrDefault(player.getUniqueId(), 0);
    }

    public static int getLastStoneMined(Player player) {
        return lastStoneMined.getOrDefault(player.getUniqueId(), 0);
    }

    public static int getLastDiamondMined(Player player) {
        return lastDiamondMined.getOrDefault(player.getUniqueId(), 0);
    }

    public static int getLastDeepslateDiamondMined(Player player) {
        return lastDeepslateDiamondMined.getOrDefault(player.getUniqueId(), 0);
    }

    public static void resetStats() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            lastStoneMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.STONE));
            lastDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
            lastDeepslateDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE));
            dailyDiamondsMined.put(uuid, 0);
        }
    }

    public static void resetPlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        lastStoneMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.STONE));
        lastDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
        lastDeepslateDiamondMined.put(uuid, getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE));
        dailyDiamondsMined.put(uuid, 0);
    }

    private static int getStatistic(Player player, Statistic statistic, Material material) {
        return player.getStatistic(statistic, material);
    }

    public static int getTotalDiamondsMined(Player player) {
        return getStatistic(player, Statistic.MINE_BLOCK, Material.DIAMOND_ORE) +
               getStatistic(player, Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE);
    }
}
