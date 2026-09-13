package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.game.running.RunningHeartbeat;
import de.varoplugin.bomberman.game.running.event.PlayerThrowBombEvent;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Particle;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;

public class ImpulsePowerupJob extends AbstractPowerupJob {

    private final Set<TNTPrimed> noDamage = new HashSet<>();

    public ImpulsePowerupJob(RunningHeartbeat heartbeat) {
        super(heartbeat, PowerupEffect.IMPULSE, Particle.CRIT);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        Set<TNTPrimed> damager = new HashSet<>();
        if (event.getDamager() instanceof TNTPrimed tntDamager) {
            damager.add(tntDamager);
        } else if (event.getDamageSource().getDirectEntity() instanceof TNTPrimed tntDamager) {
            damager.add(tntDamager);
        } else if (event.getDamageSource().getCausingEntity() instanceof TNTPrimed tntDamager) {
            damager.add(tntDamager);
        }

        if (damager.stream().noneMatch(this.noDamage::contains)) return;
        event.setDamage(0);
        this.noDamage.removeAll(damager);
    }

    @EventHandler
    public void onPlayerTouchBomb(PlayerThrowBombEvent event) {
        if (event.getPlayer().getPowerupEffect() != PowerupEffect.IMPULSE) return;
        if (event.getThrowSource() != PlayerThrowBombEvent.ThrowSource.TOUCH) return;

        this.noDamage.add(event.getBomb().getPrimed());
        event.getBomb().getPrimed().setFuseTicks(0);
    }
}
