package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.running.event.BombBounceEvent;
import de.varoplugin.bomberman.game.running.event.PlayerThrowBombEvent;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class StickyPowerupJob extends AbstractStateListenerJob {

    private final Set<Bomb> canStick = new HashSet<>();

    protected StickyPowerupJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerThrowBomb(PlayerThrowBombEvent event) {
        if (event.isCancelled()) return;
        if (event.getPlayer().getPowerupEffect() != PowerupEffect.STICKY) return;

        this.canStick.add(event.getBomb());
    }

    @EventHandler
    public void onBombBounce(BombBounceEvent event) {
        if (event.isCancelled()) return;
        if (!this.canStick.contains(event.getBomb())) return;
        if (event.getBomb().getSource().getPowerupEffect() != PowerupEffect.STICKY) return;

        event.setCancelled(true);

        this.plugin.getHeartbeat().startJobs(new AbstractStateTimerJob(this.plugin, 1, true) {
            @Override
            public void run() {
                if (!event.getBomb().getPrimed().isValid()) {
                    canStick.remove(event.getBomb());
                    this.stop();
                    return;
                }

                event.getBomb().getPrimed().setVelocity(new Vector(0, 0, 0));
            }
        });
    }
}
