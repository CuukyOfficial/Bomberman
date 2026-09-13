package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ShockwavePowerupJob extends AbstractSneakPowerupJob {

    public ShockwavePowerupJob(Bomberman plugin) {
        super(plugin, PowerupEffect.SHOCKWAVE, 5);
    }

    @Override
    boolean power(BombPlayer player) {
        float strength = player.calculateCharge();
        float radius = 2 + strength * 5;
        double knockback = 0.5 + strength * 0.8;
        int particleAmount = (int) (10 + strength * 20);

        // Effects
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        player.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION, player.getPlayer().getLocation().add(0, 1, 0), particleAmount, 0.5, 0.5, 0.5, 0);
        boolean hit = false;

        for (Entity entity : player.getPlayer().getNearbyEntities(radius, radius, radius)) {
            if (entity.equals(player.getPlayer()) || entity.isInvulnerable()) continue;

            Vector direction = entity.getLocation().toVector().subtract(player.getPlayer().getLocation().toVector());
            if (direction.lengthSquared() == 0) continue;

            entity.setVelocity(direction.normalize().multiply(knockback).setY(0.45));
            entity.getWorld().spawnParticle(Particle.EXPLOSION, entity.getLocation().add(0, 1, 0), particleAmount, 0.5, 0.5, 0.5, 0);
            hit = true;

            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(3 + strength * 4, player.getPlayer());
                entity.getWorld().playEffect(entity.getLocation(), Effect.DESTROY_BLOCK, 10);
            }
        }
        return hit;
    }
}
