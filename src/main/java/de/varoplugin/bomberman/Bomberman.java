package de.varoplugin.bomberman;

import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.events.BombermanStateSwitchEvent;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.hud.ScoreboardListener;
import de.varoplugin.bomberman.listener.PlayerListener;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Stream;

public class Bomberman extends JavaPlugin {

    private StateHeartbeat heartbeat;
    private final HashMap<Player, BombPlayer> players = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getWorlds().forEach(world -> {
            if (!world.isFixedTime()) {
                world.setTime(1000);
                world.setGameRule(GameRules.ADVANCE_TIME, false);
            }
        });

        try {
            BombermanConfig.init();
            BombermanMessages.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.switchState(GameState.LOBBY);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this), this);
    }

    @Override
    public void onDisable() {
        if (this.heartbeat != null) {
            this.heartbeat.stop();
        }
    }

    public void switchState(GameState state) {
        if (this.heartbeat != null) {
            if (this.heartbeat.getState() == state) {
                return;
            }

            this.heartbeat.stop();
        }

        System.out.println("Switching to state: " + state + (this.heartbeat != null ? " from " + this.heartbeat.getState() : ""));

        this.heartbeat = state.createHeartbeat(this);
        this.heartbeat.start();

        Bukkit.getServer().getPluginManager().callEvent(new BombermanStateSwitchEvent(state));
    }

    public StateHeartbeat getHeartbeat() {
        return this.heartbeat;
    }

    public BombPlayer getPlayer(Player player) {
        return this.players.computeIfAbsent(player, BombPlayer::new);
    }

    public Stream<BombPlayer> getPlayers() {
        return this.players.values().stream();
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }
}
