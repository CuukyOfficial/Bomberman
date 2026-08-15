package de.cuuky.bomberman.location;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LocationManager {
    private Location lobby = null;
    private HashMap<Integer, Location> spawns = new HashMap<>();
    private File file;
    private YamlConfiguration cfg;

    public LocationManager() {
        file = new File("plugins/Bomberman/", "locations.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) return;
        if (cfg.contains("lobby")) lobby = (Location) cfg.get("lobby");
        for (String str : cfg.getKeys(true)) {
            int number;
            try {
                number = Integer.valueOf(str);
            } catch (NumberFormatException e) {
                continue;
            }
            spawns.put(number, (Location) cfg.get(str));
        }
    }

    public void save() {
        if (lobby != null) cfg.set("lobby", lobby);
        if (!spawns.isEmpty()) for (int i : spawns.keySet()) cfg.set(String.valueOf(i), spawns.get(i));
        for (@SuppressWarnings("unused") String str : cfg.getKeys(true))
            try {
                cfg.save(file);
                break;
            } catch (IOException e) {
            }
    }

    public Location getLobby() {
        return this.lobby;
    }

    public void setLobby(Location loc) {
        this.lobby = loc;
    }

    public HashMap<Integer, Location> getSpawns() {
        return this.spawns;
    }

    public void addSpawn(Location loc) {
        spawns.put(spawns.size() + 1, loc);
    }
}