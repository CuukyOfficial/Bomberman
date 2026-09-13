package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BullyPowerupJob extends AbstractStateListenerJob {

    protected BullyPowerupJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPunchAnother(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        var damagerPlayer = this.plugin.getPlayer(damager);
        var victimPlayer = this.plugin.getPlayer(victim);

        if (damagerPlayer == null || victimPlayer == null) return;
        if (damagerPlayer.getPowerupEffect() != PowerupEffect.BULLY) return;
        if (event.isCancelled()) event.setCancelled(false);

        float strength = damagerPlayer.calculateCharge();

        // Knockback the victim away from the damager
        var direction = victim.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize();
        victim.setVelocity(direction.multiply(3 + strength * 3).setY(1.5));

        // Add extra damage to the victim
        event.setDamage((strength - 1) * 2);
    }
}
