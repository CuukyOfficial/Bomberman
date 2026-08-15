package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

public class LobbyHeartbeat implements StateHeartbeat {

    private Bomberman plugin;

    public LobbyHeartbeat(Bomberman plugin) {
        this.plugin = plugin;
    }

    @Override
    public Stream<Listener> createListeners() {
        return Stream.of(new LobbyCancelListener(), new NoSurvivalListener());
    }

    @Override
    public void init() {
        // Initialization logic for the lobby heartbeat
        this.plugin.getServer().broadcast(Component.text("Init"));
    }

    @Override
    public void run() {
        // Heartbeat logic for the lobby
        this.plugin.getServer().broadcast(Component.text("Run"));
    }

    @Override
    public boolean isAvailable() {
        return true; // Check if setup is done, if not, return false to prevent the game from starting
    }
}
