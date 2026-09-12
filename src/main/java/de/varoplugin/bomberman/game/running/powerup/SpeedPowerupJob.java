package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerupJob extends AbstractStateTimerJob {

    public SpeedPowerupJob(Bomberman plugin) {
        super(plugin, 1, true);
    }

    @Override
    public void run() {
        this.plugin.getAlive().forEach(player -> {
            if (player.getPowerupEffect() != PowerupEffect.SPEED) {
                player.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                return;
            }

            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, false, false, false));
        });
    }
}
