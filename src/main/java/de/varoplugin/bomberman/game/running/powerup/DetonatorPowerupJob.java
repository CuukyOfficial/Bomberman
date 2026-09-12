package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;

import java.util.Comparator;
import java.util.stream.Stream;

public class DetonatorPowerupJob extends AbstractSneakPowerupJob {

    protected DetonatorPowerupJob(Bomberman plugin) {
        super(plugin, PowerupEffect.DETONATOR, 6);
    }

    @Override
    void power(BombPlayer player) {
        float strength = player.calculateCharge();
        int amountOfBombs = (int) (1 + strength * 3);
        Stream<Bomb> bombs = player.getBombs().sorted(Comparator.comparingInt(bomb -> bomb.getPrimed().getFuseTicks()))
                .limit(amountOfBombs);
        bombs.forEach(bomb -> bomb.getPrimed().setFuseTicks(0));
    }
}
