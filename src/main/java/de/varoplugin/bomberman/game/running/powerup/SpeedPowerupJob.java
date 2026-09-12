package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.running.event.PowerupCollectEvent;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerupJob extends AbstractStateListenerJob {

    public SpeedPowerupJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPowerupCollected(PowerupCollectEvent event) {
        if (event.getEffect() != PowerupEffect.SPEED) {
            if (event.getPlayer().getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
                event.getPlayer().getPlayer().removePotionEffect(PotionEffectType.SPEED);
            }
            return;
        }

        var player = event.getPlayer();
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, false, true, false));
    }
}
