package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

public class StartingHeartbeat implements StateHeartbeat {

    private final Bomberman plugin;
    private int countdown;

    public StartingHeartbeat(Bomberman plugin) {
        this.plugin = plugin;
    }

    @Override
    public Stream<Listener> createListeners() {
        return Stream.of(new StartAbortListener(this.plugin), new LobbyCancelListener());
    }

    @Override
    public void init() {
        this.countdown = BombermanConfig.LOBBY_DELAY.getValue();
    }

    @Override
    public void run() {
        if (this.countdown == 0) {
            this.plugin.switchState(GameState.RUNNING);
            return;
        }

        if (this.countdown % 5 == 0 || this.countdown <= 5) {
            Bukkit.broadcastMessage("Das Spiel startet in " + this.countdown + " Sekunden!");
        }
        this.countdown--;
    }
}
