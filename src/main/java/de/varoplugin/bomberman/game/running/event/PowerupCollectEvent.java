package de.varoplugin.bomberman.game.running.event;

import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PowerupCollectEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BombPlayer player;
    private final PowerupEffect effect;

    public PowerupCollectEvent(BombPlayer player, PowerupEffect effect) {
        this.player = player;
        this.effect = effect;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public BombPlayer getPlayer() {
        return player;
    }

    public PowerupEffect getEffect() {
        return effect;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
