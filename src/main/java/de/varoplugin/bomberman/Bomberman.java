package de.varoplugin.bomberman;

import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.hud.ScoreboardListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Bomberman extends JavaPlugin {

    private GameState state;
    private BukkitTask heartbeatTask;
    private List<Listener> listeners;

    public void switchState(GameState state) {
        if (this.state == state) return;

        StateHeartbeat heartbeat = state.createHeartbeat(this);
        if (this.state != null) {
            this.heartbeatTask.cancel();
            this.listeners.forEach(HandlerList::unregisterAll);
        }

        this.state = state;
        heartbeat.init();
        this.listeners = heartbeat.createListeners().collect(Collectors.toList());
        this.listeners.forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
        this.heartbeatTask = this.getServer().getScheduler().runTaskTimer(this, heartbeat::run, 0L, 20L);
    }

    public GameState getState() {
        return state;
    }

    @Override
    public void onEnable() {
        try {
            BombermanConfig.init();
            BombermanMessages.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this), this); // TODO

        this.switchState(GameState.LOBBY);
    }

    @Override
    public void onDisable() {
        if (this.heartbeatTask != null) {
            this.heartbeatTask.cancel();
        }
    }
}
