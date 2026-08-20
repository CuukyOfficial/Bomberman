package de.varoplugin.bomberman.model;

import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import org.bukkit.entity.Player;

public class BombPlayer {

    private final Player player;
    private PlayerType type;
    private Powerup powerup;
    private AnimatedScoreboard scoreboard;

    public BombPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public AnimatedScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(AnimatedScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
}
