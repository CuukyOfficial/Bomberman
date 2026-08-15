package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.lobby.LobbyHeartbeat;
import de.varoplugin.bomberman.game.lobby.StartingHeartbeat;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;

import java.util.function.Function;

public enum GameState {

    LOBBY(LobbyHeartbeat::new),
    STARTING(StartingHeartbeat::new),
    RUNNING(RunningHeartbeat::new);

    private final Function<Bomberman, StateHeartbeat> taskFunction;

    GameState(Function<Bomberman, StateHeartbeat> taskFunction) {
        this.taskFunction = taskFunction;
    }

    public StateHeartbeat createHeartbeat(Bomberman plugin) {
        return taskFunction.apply(plugin);
    }
}
