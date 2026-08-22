package de.varoplugin.bomberman.model;

public enum PowerupEffect {

    PUNCH,
    SPEED,
    SHOCKWAVE,
    FREEZE,
    CARRY;

    public static PowerupEffect random() {
        return values()[(int) (Math.random() * values().length)];
    }
}
