package de.varoplugin.bomberman.model;

import de.varoplugin.cfw.world.Hologram;
import org.bukkit.entity.Entity;

public class PowerupItem {

    private final Entity crystal;
    private final PowerupEffect effect;
    private final Hologram hologram;

    public PowerupItem(Entity crystal, PowerupEffect effect, Hologram hologram) {
        this.crystal = crystal;
        this.effect = effect;
        this.hologram = hologram;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public PowerupEffect getEffect() {
        return effect;
    }

    public void remove() {
        this.crystal.remove();
        this.hologram.remove();
    }
}
