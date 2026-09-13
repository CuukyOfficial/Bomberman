package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.running.event.BombBounceEvent;
import de.varoplugin.bomberman.game.running.event.PlayerThrowBombEvent;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class StickyPowerupJob extends AbstractStateListenerJob {

    private final Map<Bomb, Location> stick = new HashMap<>();

    protected StickyPowerupJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerThrowBomb(PlayerThrowBombEvent event) {
        if (event.isCancelled()) return;
        if (event.getPlayer().getPowerupEffect() != PowerupEffect.STICKY) return;

        this.stick.put(event.getBomb(), event.getBomb().getPrimed().getLocation());
    }

    @EventHandler
    public void onBombBounce(BombBounceEvent event) {
        if (event.isCancelled()) return;
        if (!this.stick.containsKey(event.getBomb())) return;
        if (event.getBomb().getSource().getPowerupEffect() != PowerupEffect.STICKY) return;

        event.setCancelled(true);
        this.stick.put(event.getBomb(), event.getBomb().getPrimed().getLocation());

        this.plugin.getHeartbeat().startJobs(new AbstractStateTimerJob(this.plugin, 1, false) {
            @Override
            public void run() {
                if (!event.getBomb().getPrimed().isValid()) {
                    stick.remove(event.getBomb());
                    this.stop();
                    return;
                }

                event.getBomb().getPrimed().getWorld().spawnParticle(Particle.ITEM_SLIME, event.getBomb().getPrimed().getLocation(),
                        2, 0.2, 0.2, 0.2, 0.1);

                event.getBomb().getPrimed().setGravity(false);
                event.getBomb().getPrimed().setVelocity(new Vector(0, 0, 0));
                event.getBomb().getPrimed().teleport(stick.get(event.getBomb()));
            }
        });
    }
}
