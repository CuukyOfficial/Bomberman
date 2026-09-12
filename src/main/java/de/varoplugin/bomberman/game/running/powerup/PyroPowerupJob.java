package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

public class PyroPowerupJob extends AbstractStateListenerJob {

    protected PyroPowerupJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof TNTPrimed tnt)) return;

        Bomb bomb = this.plugin.getBomb(tnt);
        if (bomb == null) return;
        if (bomb.getSource().getPowerupEffect() != PowerupEffect.PYRO) return;
        float strength = bomb.getSource().calculateCharge();

        int fireCount = (int) (5 + strength * 20);
        float radius = 3 + strength * 5;
        int duration = (int) (50 + strength * 50);

        // Add fire particles in 5m radius around the explosion
        event.blockList().forEach(block -> block.getWorld().spawnParticle(Particle.FLAME, block.getLocation(), fireCount, radius / 2, radius / 2, radius / 2, 0.1));

        // Burn all entities in 5m radius around the explosion for 3 seconds
        tnt.getWorld().getNearbyEntities(tnt.getLocation(), radius, radius, radius).forEach(entity -> {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setFireTicks(duration);
            }
        });
    }
}
