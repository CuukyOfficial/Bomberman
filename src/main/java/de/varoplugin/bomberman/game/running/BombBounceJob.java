package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.running.event.BombBounceEvent;
import org.bukkit.Sound;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BombBounceJob extends AbstractStateTimerJob {

    private final Map<TNTPrimed, Vector> velocities = new HashMap<>();

    public BombBounceJob(Bomberman plugin) {
        super(plugin, 1, false);
    }

    @Override
    public void run() {
        this.plugin.getBombs().forEach(bomb -> {
            TNTPrimed tntEntity = bomb.getPrimed();
            if (!tntEntity.isValid()) {
                velocities.remove(tntEntity);
                return;
            }

            if (!velocities.containsKey(tntEntity)) {
                velocities.put(tntEntity, tntEntity.getVelocity().clone());
            }
            Vector prevVelocity = velocities.get(tntEntity);

            Vector currentVelocity = tntEntity.getVelocity();
            double prevX = prevVelocity.getX();
            double currX = currentVelocity.getX();
            double prevZ = prevVelocity.getZ();
            double currZ = currentVelocity.getZ();
            double prevY = prevVelocity.getY();
            double currY = currentVelocity.getY();

            boolean bounced = false;
            double bounceFactor = 0.65;

            if (Math.abs(prevX) > 0.1 && Math.abs(currX) < 0.01) {
                currentVelocity.setX(-prevX * bounceFactor);
                bounced = true;
            }
            if (Math.abs(prevZ) > 0.1 && Math.abs(currZ) < 0.01) {
                currentVelocity.setZ(-prevZ * bounceFactor);
                bounced = true;
            }
            if (Math.abs(prevY) > 0.1 && Math.abs(currY) < 0.01) {
                currentVelocity.setY(-prevY * bounceFactor);
                bounced = true;
            }

            if (bounced) {
                BombBounceEvent event = new BombBounceEvent(bomb);
                this.plugin.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;

                tntEntity.setVelocity(currentVelocity);
                this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
                        tntEntity.getWorld().playSound(tntEntity.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.0f, 1.2f));
            }

            velocities.put(tntEntity, tntEntity.getVelocity().clone());
        });
    }
}
