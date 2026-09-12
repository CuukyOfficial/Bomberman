package de.varoplugin.bomberman.model;

import org.bukkit.entity.TNTPrimed;

public class Bomb {

    private final BombPlayer source;
    private final TNTPrimed primed;
    private BombPlayer lastTouched;

    public Bomb(BombPlayer source, TNTPrimed primed) {
        this.source = source;
        this.primed = primed;
        this.lastTouched = source;
    }

    public TNTPrimed getPrimed() {
        return primed;
    }

    public BombPlayer getSource() {
        return source;
    }

    public int getRemainingSeconds() {
        return this.primed.getFuseTicks() / 20;
    }

    public void setLastTouched(BombPlayer lastTouched) {
        this.lastTouched = lastTouched;
    }
}
