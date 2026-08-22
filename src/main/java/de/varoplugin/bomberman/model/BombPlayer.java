package de.varoplugin.bomberman.model;

import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import org.bukkit.entity.Player;

public class BombPlayer {

    private final Player player;
    private PlayerType type;
    private Powerup powerup;
    private AnimatedScoreboard scoreboard;
    private long sneakingSince;

    public BombPlayer(Player player) {
        this.player = player;
        this.type = PlayerType.ALIVE;
    }

    public boolean isAlive() {
        return this.type == PlayerType.ALIVE;
    }

    public Player getPlayer() {
        return player;
    }

    public long getSneakingSince() {
        return sneakingSince;
    }

    public void setSneakingSince(long sneakingSince) {
        this.sneakingSince = sneakingSince;
    }

    public AnimatedScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(AnimatedScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
}
