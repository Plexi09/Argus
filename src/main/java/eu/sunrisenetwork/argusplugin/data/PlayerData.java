package eu.sunrisenetwork.argusplugin.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private static Map<UUID, Long> lastLoginTimes = new HashMap<>();

    public static void setLastLogin(UUID playerId, long time) {
        lastLoginTimes.put(playerId, time);
    }

    public static long getLastLogin(UUID playerId) {
        return lastLoginTimes.getOrDefault(playerId, 0L);
    }
}
