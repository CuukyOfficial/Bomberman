package de.varoplugin.bomberman.game.running.event;

import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerUp;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerPowerupChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BombPlayer player;
    private final PowerUp powerUp;

    public PlayerPowerupChangeEvent(BombPlayer player, PowerUp powerUp) {
        this.player = player;
        this.powerUp = powerUp;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public BombPlayer getPlayer() {
        return player;
    }

    public PowerupEffect getEffect() {
        return powerUp != null ? powerUp.getEffect() : null;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
