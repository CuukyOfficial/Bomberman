package de.varoplugin.bomberman.model;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import de.varoplugin.cfw.player.hud.ScoreboardInstance;
import org.bukkit.GameMode;
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

    public void enableSpectator(Bomberman plugin) {
        this.setType(PlayerType.SPECTATOR);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20);
        player.getInventory().clear();
        player.setFallDistance(0);
        player.setNoDamageTicks(0);
        player.setFireTicks(0);
        player.setInvulnerable(false);

        for (BombPlayer alive : plugin.getAlive().toList()) {
            if (!alive.isAlive()) continue;
            alive.getPlayer().hidePlayer(plugin, this.player);
        }
    }

    public PlayerType getType() {
        return type;
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
