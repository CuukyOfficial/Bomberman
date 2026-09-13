package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.running.event.PlayerPowerupChangeEvent;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.event.EventHandler;

public abstract class AbstractPowerupJob extends AbstractStateListenerJob {

    protected final PowerupEffect effect;

    protected AbstractPowerupJob(Bomberman plugin, PowerupEffect effect) {
        super(plugin);
        this.effect = effect;
    }

    protected void onActivate(BombPlayer player) {
        // Default implementation does nothing
    }

    protected void onDeactivate(BombPlayer player) {
        // Default implementation does nothing
    }

    @Override
    public void stop() {
        super.stop();

        this.plugin.getAlive().forEach(player -> {
            if (player.getPowerupEffect() == this.effect) {
                this.onDeactivate(player);
            }
        });
    }

    @EventHandler
    public void onPlayerPowerupChange(PlayerPowerupChangeEvent event) {
        BombPlayer player = event.getPlayer();
        if (event.getEffect() == this.effect) {
            this.onActivate(player);
        } else if (player.getPowerupEffect() == this.effect) {
            this.onDeactivate(player);
        }
    }
}
