package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.hud.ScoreboardListener;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RunningHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    private final Map<Player, BombPlayer> players = new HashMap<>();

    public RunningHeartbeat(Bomberman plugin) {
        super(plugin);

        this.registerJobs(new BombListener(this.plugin), new NoSurvivalListener(this.plugin),
                new ScoreboardListener(this.plugin, BombermanMessages.SCOREBOARD_GAME),
                new PlayerGameStateJob(this),
                new GameEndListener(this.plugin));
    }

    @Override
    public GameState getState() {
        return GameState.RUNNING;
    }

    @Override
    public void start() {
        super.start();

        Bukkit.broadcastMessage("§7Das Spiel hat begonnen!");
    }

    @Override
    public void run() {

    }

    public BombPlayer getPlayer(Player player) {
        return this.players.get(player);
    }

    public BombPlayer addPlayer(Player player) {
        BombPlayer bombPlayer = new BombPlayer(player);
        this.players.put(player, bombPlayer);
        return bombPlayer;
    }
}
