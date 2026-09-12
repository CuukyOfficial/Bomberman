package de.varoplugin.bomberman.game.running.event;

import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerPunchBombEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerAnimationEvent source;
    private final BombPlayer player;
    private final Bomb bomb;
    private boolean cancelled = false;

    public PlayerPunchBombEvent(PlayerAnimationEvent source, BombPlayer player, Bomb bomb) {
        this.source = source;
        this.player = player;
        this.bomb = bomb;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public PlayerAnimationEvent getSource() {
        return source;
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

}
