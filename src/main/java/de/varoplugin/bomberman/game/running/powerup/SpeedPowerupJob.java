package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerupJob extends AbstractPowerupJob {

    public SpeedPowerupJob(Bomberman plugin) {
        super(plugin, PowerupEffect.SPEED);
    }

    @Override
    protected void onActivate(BombPlayer player) {
        super.onActivate(player);

        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, false, true, false));
    }

    @Override
    protected void onDeactivate(BombPlayer player) {
        super.onDeactivate(player);

        player.getPlayer().removePotionEffect(PotionEffectType.SPEED);
    }
}
