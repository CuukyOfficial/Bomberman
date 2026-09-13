package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.SynchronousTimerTask;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicBoolean;

public class FreezePowerupJob extends AbstractSneakPowerupJob {

    public FreezePowerupJob(Bomberman plugin) {
        super(plugin, PowerupEffect.FREEZE, 5);
    }

    @Override
    boolean power(BombPlayer player) {
        // Give all nearby entities a slowness effect and play a sound and particle effect
        float strength = player.calculateCharge();
        float radius = 2 + strength * 5;
        int particleAmount = (int) (10 + strength * 20);
        int duration = (int) (2 + strength * 5) * 20;
        int amplifier = (int) (1 + strength * 4);
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_SNOW_GOLEM_HURT, 1.0f, 1.0f);
        // Effects
        player.getPlayer().getWorld().spawnParticle(Particle.SNOWFLAKE, player.getPlayer().getLocation().add(0, 1, 0), particleAmount, 0.5, 0.5, 0.5, 0);
        AtomicBoolean hit = new AtomicBoolean(false);
        player.getPlayer().getNearbyEntities(radius, radius, radius).forEach(entity -> {
            if (!entity.equals(player.getPlayer()) && entity.getVelocity().length() > 0) {
                entity.setVelocity(new Vector(0, 0, 0));

                entity.getWorld().spawnParticle(Particle.SNOWFLAKE, entity.getLocation().add(0, 1, 0), particleAmount, 0.5, 0.5, 0.5, 0);

                if (entity instanceof LivingEntity lv) {
                    lv.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier, false, false, false));
                    hit.set(true);

                    this.plugin.getHeartbeat().startJobs(new SynchronousTimerTask(plugin, 5, () -> {
                        lv.getWorld().spawnParticle(Particle.SNOWFLAKE, lv.getLocation().add(0, 1, 0), particleAmount, 0.5, 0.5, 0.5, 0);
                    }));

                    if (lv instanceof Player p) {
                        p.playSound(entity.getLocation(), Sound.ENTITY_SNOW_GOLEM_HURT, 1.0f, 1.0f);
                    }
                }
            }
        });
        return hit.get();
    }
}
