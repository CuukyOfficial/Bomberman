package de.varoplugin.bomberman.model;

import org.bukkit.entity.Player;

public class BombPlayer {

    private final Player player;
    private PlayerType type;
    private Powerup powerup;

    public BombPlayer(Player player) {
        this.player = player;
    }
}
