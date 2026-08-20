package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;

public interface StateHeartbeat extends StateJob, Runnable {

    GameState getState();

    void registerJobs(StateJob... jobs);

    void startJobs(StateJob... jobs);

    Bomberman getPlugin();

}
