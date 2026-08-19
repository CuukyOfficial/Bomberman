package de.varoplugin.bomberman;

import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.hud.ScoreboardListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Bomberman extends JavaPlugin {

    private StateHeartbeat heartbeat;

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

        this.heartbeat = state.createHeartbeat(this);
        this.heartbeat.start();
    }

    public StateHeartbeat getHeartbeat() {
        return this.heartbeat;
    }
}
