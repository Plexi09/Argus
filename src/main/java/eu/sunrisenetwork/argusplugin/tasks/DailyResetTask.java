package eu.sunrisenetwork.argusplugin.tasks;

import eu.sunrisenetwork.argusplugin.ARGUS;
import eu.sunrisenetwork.argusplugin.data.LuckCalculator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DailyResetTask extends BukkitRunnable {

    private final JavaPlugin plugin;

    public DailyResetTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getLogger().info("Resetting player stats...");
        LuckCalculator.resetStats();
    }

    public void start() {
        // Schedule to run every topluckDataResetInterval ticks
        long intervalTicks = ARGUS.topluckDataResetInterval;
        this.runTaskTimer(plugin, intervalTicks, intervalTicks);
    }
}
