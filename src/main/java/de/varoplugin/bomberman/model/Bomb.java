package de.varoplugin.bomberman.model;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class Bomb {

    private final Player source;
    private final TNTPrimed primed;
    private Player lastTouched;

    public Bomb(Player source, TNTPrimed primed) {
        this.source = source;
        this.primed = primed;
        this.lastTouched = source;
    }

    public TNTPrimed getPrimed() {
        return primed;
    }

    public Player getSource() {
        return source;
    }

    public int getRemainingSeconds() {
        return this.primed.getFuseTicks() / 20;
    }

    public void setLastTouched(Player lastTouched) {
        this.lastTouched = lastTouched;
    }
}
