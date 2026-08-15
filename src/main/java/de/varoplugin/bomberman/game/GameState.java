package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.lobby.LobbyHeartbeat;

import java.util.function.Function;

public enum GameState {

    LOBBY(LobbyHeartbeat::new);

    private Function<Bomberman, StateHeartbeat> taskFunction;

    GameState(Function<Bomberman, StateHeartbeat> taskFunction) {
        this.taskFunction = taskFunction;
    }

    public StateHeartbeat createHeartbeat(Bomberman plugin) {
        return taskFunction.apply(plugin);
    }
}
