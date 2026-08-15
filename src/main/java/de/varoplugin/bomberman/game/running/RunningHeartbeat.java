package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.StateHeartbeat;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

public class RunningHeartbeat implements StateHeartbeat {

    private final Bomberman plugin;

    public RunningHeartbeat(Bomberman plugin) {
        this.plugin = plugin;
    }

    @Override
    public Stream<Listener> createListeners() {
        return Stream.empty();
    }

    @Override
    public void init() {
        Bukkit.broadcastMessage("§7Das Spiel hat begonnen!");
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
