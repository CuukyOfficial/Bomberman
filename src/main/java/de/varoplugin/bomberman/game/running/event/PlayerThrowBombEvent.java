package de.varoplugin.bomberman.game.running.event;

import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerThrowBombEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final BombPlayer player;
    private final Bomb bomb;
    private final ThrowSource throwSource;
    private boolean cancelled = false;
    public PlayerThrowBombEvent(BombPlayer player, ThrowSource throwSource, Bomb bomb) {
        this.player = player;
        this.throwSource = throwSource;
        this.bomb = bomb;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public ThrowSource getThrowSource() {
        return throwSource;
    }

    public BombPlayer getPlayer() {
        return player;
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

    public enum ThrowSource {
        PUNCH, TOUCH
    }

}
