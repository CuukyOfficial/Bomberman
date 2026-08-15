package de.varoplugin.bomberman.game;

import org.bukkit.event.Listener;

import java.util.stream.Stream;

public interface StateHeartbeat {

    Stream<Listener> createListeners();

    void init();

    void run();

    boolean isAvailable();

}
