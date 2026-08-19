package de.varoplugin.bomberman.game;

public interface StateHeartbeat extends StateJob, Runnable {

    GameState getState();

    void registerJobs(StateJob... jobs);

    void startJobs(StateJob... jobs);

}
