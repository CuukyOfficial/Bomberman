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

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
