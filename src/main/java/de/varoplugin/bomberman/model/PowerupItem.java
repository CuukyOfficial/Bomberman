package de.varoplugin.bomberman.model;

import de.varoplugin.cfw.world.Hologram;
import org.bukkit.entity.Entity;

public class PowerupItem {

    private final Entity item;
    private final Hologram hologram;
    private final long spawnTime;

    public PowerupItem(Entity item, Hologram hologram, long spawnTime) {
        this.item = item;
        this.hologram = hologram;
        this.spawnTime = spawnTime;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public void remove() {
        this.item.remove();
        this.hologram.remove();
    }
}
