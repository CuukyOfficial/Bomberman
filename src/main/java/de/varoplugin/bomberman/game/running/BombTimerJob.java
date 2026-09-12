package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.Bomb;
import org.bukkit.Particle;

import java.util.Comparator;

public class BombTimerJob extends AbstractStateTimerJob {

    public BombTimerJob(Bomberman plugin) {
        super(plugin, 10, true);
    }

    @Override
    public void run() {
        this.plugin.getAlive().forEach(player -> {
            int time = player.getBombs().map(Bomb::getRemainingSeconds).min(Comparator.naturalOrder()).orElse(0);
            player.getPlayer().setLevel(time);

            player.getBombs().map(Bomb::getPrimed).forEach(tntEntity ->
                    tntEntity.getWorld().spawnParticle(Particle.LARGE_SMOKE, tntEntity.getLocation().add(0, 0.5, 0), 2, 0.1, 0.1, 0.1, 0.01));
        });
    }

    @Override
    public void stop() {
        super.stop();

        this.plugin.getBombs().forEach(bomb -> {
            bomb.getSource().getPlayer().setLevel(0);
            bomb.getSource().getPlayer().setExp(0);
            bomb.getPrimed().remove();
        });
    }
}
