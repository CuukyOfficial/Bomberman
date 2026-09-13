package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;
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

    public FreezePowerupJob(RunningHeartbeat heartbeat) {
        super(heartbeat, PowerupEffect.FREEZE, Particle.SNOWFLAKE, 5);
    }

    @Override
    boolean power(BombPlayer player) {
        // Give all nearby entities a slowness effect and play a sound and particle effect
        float strength = player.calculateCharge();
        float radius = 2 + strength * 5;
        int particleAmount = (int) (10 + strength * 20);
        int duration = (int) (2 + strength * 5) * 20;
        int amplifier = (int) (1 + strength * 4);
        // Effects
        AtomicBoolean hit = new AtomicBoolean(false);
        player.getPlayer().getNearbyEntities(radius, radius, radius).forEach(entity -> {
            if (!entity.equals(player.getPlayer()) && !entity.isInvulnerable()) {
                entity.setVelocity(new Vector(0, 0, 0));

                entity.getWorld().spawnParticle(Particle.SNOWFLAKE, entity.getLocation().add(0, 1, 0), particleAmount, 0.5, 0.5, 0.5, 0);

                if (entity instanceof LivingEntity lv) {
                    lv.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier, false, false, false));
                    hit.set(true);
                    long until = System.currentTimeMillis() + duration * 50L; // 50ms per tick

                    this.plugin.getHeartbeat().startJobs(new AbstractStateTimerJob(plugin, 5) {
                        @Override
                        public void run() {
                            if (!lv.isValid() || System.currentTimeMillis() > until) {
                                this.stop();
                                return;
                            }

                            lv.getWorld().spawnParticle(Particle.SNOWFLAKE, lv.getLocation().add(0, 0.3, 0), particleAmount / 2, 0.5, 0.5, 0.5, 0);

                        }
                    });

                    if (lv instanceof Player p) {
                        p.playSound(entity.getLocation(), Sound.ENTITY_SNOW_GOLEM_HURT, 1.0f, 1.0f);
                    }
                }
            }
        });

        if (hit.get()) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_SNOW_GOLEM_HURT, 1.0f, 1.0f);
        }
        return hit.get();
    }
}
