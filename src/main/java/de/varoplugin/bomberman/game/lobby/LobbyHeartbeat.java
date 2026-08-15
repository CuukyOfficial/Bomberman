package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

public class LobbyHeartbeat implements StateHeartbeat {

    private final Bomberman plugin;
    private int count;

    public LobbyHeartbeat(Bomberman plugin) {
        this.plugin = plugin;
    }

    @Override
    public Stream<Listener> createListeners() {
        return Stream.of(new LobbyCancelListener(), new NoSurvivalListener(), new LobbyStartListener(this.plugin));
    }

    @Override
    public void init() {
        this.count = 0;
    }

    @Override
    public void run() {
        if (this.count >= 10) {
            Bukkit.broadcastMessage("Waiting for players...");
            this.count = 0;
        } else {
            this.count++;
        }
    }
}
