package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.Bomb;
import org.bukkit.Particle;

public class BombTimerJob extends AbstractStateTimerJob {

    public BombTimerJob(Bomberman plugin) {
        super(plugin, 10, true);
    }

    @Override
    public void run() {
        plugin.getBombs().map(Bomb::getPrimed).forEach(tntEntity ->
                tntEntity.getWorld().spawnParticle(Particle.LARGE_SMOKE, tntEntity.getLocation().add(0, 0.5, 0), 2, 0.1, 0.1, 0.1, 0.01));
    }

    @Override
    public void stop() {
        super.stop();

        this.plugin.getBombs().forEach(bomb -> bomb.getPrimed().remove());
    }
}
