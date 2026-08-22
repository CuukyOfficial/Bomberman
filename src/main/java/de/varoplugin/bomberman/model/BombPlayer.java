package de.varoplugin.bomberman.model;

import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import de.varoplugin.cfw.player.hud.ScoreboardInstance;
import org.bukkit.entity.Player;

public class BombPlayer {

    private final Player player;
    private PlayerType type;
    private Powerup powerup;
    private ScoreboardInstance scoreboardInstance;
    private AnimatedScoreboard scoreboard;
    private long sneakingSince;

    public BombPlayer(Player player) {
        this.player = player;
        this.type = PlayerType.ALIVE;
    }

    public boolean isAlive() {
        return this.type == PlayerType.ALIVE;
    }

    public void setType(PlayerType type) {
        this.type = type;
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

    public ScoreboardInstance getScoreboardInstance() {
        return scoreboardInstance;
    }

    public void setScoreboard(AnimatedScoreboard scoreboard, ScoreboardInstance scoreboardInstance) {
        this.scoreboardInstance = scoreboardInstance;
        this.scoreboard = scoreboard;
    }
}
