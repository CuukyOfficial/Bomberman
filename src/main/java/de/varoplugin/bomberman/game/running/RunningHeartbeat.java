package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMap;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.model.BombPlayer;

import java.util.ArrayList;
import java.util.Collections;

public class RunningHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    private int countdown;

    public RunningHeartbeat(Bomberman plugin) {
        super(plugin);

        this.countdown = 600;
        this.registerJobs(new BombListener(this.plugin), new NoSurvivalListener(this.plugin),
                new PlayerGameStateJob(this),
                new GameDeathListener(this.plugin),
                new RunningCancelListener(this.plugin),
                new SpectatorListener(this.plugin),
                new BombTimerJob(this.plugin),
                new BombBounceJob(this.plugin));
    }

    @Override
    public GameState getState() {
        return GameState.RUNNING;
    }

    @Override
    public void start() {
        super.start();

        BombermanMessages.broadcast(BombermanMessages.GAME_START, this.plugin);

        BombermanMap map = findMap();
        int i = 0;
        for (BombPlayer player : this.plugin.getAlive().toList()) {
            // TODO spectators
            player.getPlayer().teleport(map.spawns.get(i++));
        }
    }

    private BombermanMap findMap() {
        long numPlayers = this.plugin.getPlayers().count();

        var maps = new ArrayList<>(BombermanConfig.MAPS.getValue());
        Collections.shuffle(maps);

        BombermanMap optimal = null;
        int optimal_players = Integer.MAX_VALUE;
        for (var map : maps) {
            if (map.spawns.size() < numPlayers)
                continue;

            if (map.spawns.size() == numPlayers)
                return map;

            if (optimal == null || map.spawns.size() < optimal_players) {
                optimal = map;
                optimal_players = map.spawns.size();
            }
        }

        if (optimal == null)
            throw new IllegalStateException("No map found for " + numPlayers + " players!");

        return optimal;
    }

    public int getCountdown() {
        return this.countdown;
    }

    @Override
    public void run() {
        if (this.countdown == 0) {
            this.plugin.switchState(GameState.FINISHED);
            return;
        }

        this.countdown--;
    }
}
