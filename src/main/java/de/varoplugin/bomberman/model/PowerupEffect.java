package de.varoplugin.bomberman.model;

public enum PowerupEffect {

    NOTHING;

    public static PowerupEffect random() {
        return values()[(int) (Math.random() * values().length)];
    }
}
