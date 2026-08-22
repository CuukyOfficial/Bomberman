package de.varoplugin.bomberman.model;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import de.varoplugin.cfw.player.hud.ScoreboardInstance;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BombPlayer {

    private final Player player;
    private PlayerType type;
    private Powerup powerup;
    private ScoreboardInstance scoreboardInstance;
    private AnimatedScoreboard scoreboard;
    private long sneakingSince;
    private final Map<TNTPrimed, Bomb> bombs;

    public BombPlayer(Player player) {
        this.player = player;
        this.type = PlayerType.ALIVE;
        this.bombs = new HashMap<>();
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

    public Player getPlayer() {
        return player;
    }

    public boolean isAlive() {
        return this.type == PlayerType.ALIVE;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public Stream<Bomb> getBombs() {
        return bombs.values().stream();
    }

    public void addBomb(Bomb bomb) {
        this.bombs.put(bomb.getPrimed(), bomb);
    }

    public Bomb removeBomb(TNTPrimed bomb) {
        return this.bombs.remove(bomb);
    }

    public Bomb getBomb(TNTPrimed primed) {
        return this.bombs.get(primed);
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
