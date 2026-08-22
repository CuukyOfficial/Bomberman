package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.model.PlayerType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SpectatorListener extends AbstractStateListenerJob {

    public SpectatorListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpectatorDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (this.plugin.getPlayer(player).getType() == PlayerType.SPECTATOR) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpectatorDoDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (this.plugin.getPlayer(player).getType() == PlayerType.SPECTATOR) {
                event.setCancelled(true);
            }
        }
    }
}
