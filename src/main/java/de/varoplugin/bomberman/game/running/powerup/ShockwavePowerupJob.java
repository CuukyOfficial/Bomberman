package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShockwavePowerupJob extends AbstractSneakPowerupJob {

    public ShockwavePowerupJob(Bomberman plugin) {
        super(plugin, PowerupEffect.SHOCKWAVE, 5);
    }

    @Override
    void power(BombPlayer player) {
        float strength = player.calculateCharge();
        float radius = 2 + strength * 5;
        double knockback = 0.5 + strength * 0.8;

        for (Entity entity : player.getPlayer().getNearbyEntities(radius, radius, radius)) {
            if (entity.equals(player.getPlayer())) continue;

            Vector direction = entity.getLocation().toVector().subtract(player.getPlayer().getLocation().toVector());
            if (direction.lengthSquared() == 0) continue;

            entity.setVelocity(direction.normalize().multiply(knockback).setY(0.45));
            entity.getWorld().spawnParticle(Particle.EXPLOSION, entity.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0);

            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(3 + strength * 4, player.getPlayer());

                if (entity instanceof Player target) {
                    target.playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                }
            }
        }
    }
}
