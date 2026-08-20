package de.varoplugin.bomberman.events;

import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public class BombermanStateSwitchEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final GameState state;

    public BombermanStateSwitchEvent(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
