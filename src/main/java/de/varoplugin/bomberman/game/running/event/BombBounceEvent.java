package de.varoplugin.bomberman.game.running.event;

import de.varoplugin.bomberman.model.Bomb;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BombBounceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Bomb bomb;
    private boolean cancelled = false;

    public BombBounceEvent(Bomb bomb) {
        this.bomb = bomb;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Bomb getBomb() {
        return bomb;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
