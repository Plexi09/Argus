package eu.sunrisenetwork.argusplugin.data;

import org.bukkit.GameMode;
import org.bukkit.Location;

public class PlayerState {
    private GameMode gameMode;
    private Location location;

    public PlayerState(GameMode gameMode, Location location) {
        this.gameMode = gameMode;
        this.location = location;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Location getLocation() {
        return location;
    }
}
