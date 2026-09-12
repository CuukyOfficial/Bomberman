package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.running.event.PlayerPunchBombEvent;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CarryPowerupJob extends AbstractStateTimerJob {

    private final Map<BombPlayer, CarryingBomb> carryingPlayers = new ConcurrentHashMap<>();

    public CarryPowerupJob(Bomberman plugin) {
        super(plugin, 1, true);
    }

    @EventHandler
    public void onPlayerPunchBomb(PlayerPunchBombEvent event) {
        BombPlayer player = event.getPlayer();
        if (player.getPowerupEffect() != PowerupEffect.CARRY) return;
        event.setCancelled(true);

        CarryingBomb carrying = this.carryingPlayers.get(player);
        if (carrying == null) {
            Bomb bomb = event.getBomb();
            TNTPrimed tnt = bomb.getPrimed();
            double distance = tnt.getLocation().distance(player.getPlayer().getEyeLocation());
            this.carryingPlayers.put(player, new CarryingBomb(bomb, tnt.getFuseTicks(), distance));
        } else {
            this.carryingPlayers.remove(player);
        }
    }

    @Override
    public void run() {
        // make that tnt follows the player wherever je walks or looks with the tnt and freezes the ticks until he is done
        this.carryingPlayers.forEach((player, bomb) -> {
            // Update the position of the bomb to follow the player where he looks
            Player p = player.getPlayer();
            TNTPrimed tntEntity = bomb.bomb.getPrimed();
            Location whereTo = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(bomb.distance));
            double distance = tntEntity.getLocation().distance(whereTo);

            // Remove if distance too far? (got kicked off)
            Vector to = whereTo.toVector().subtract(tntEntity.getLocation().toVector()).normalize().multiply(distance);

            tntEntity.setVelocity(to);
            tntEntity.setFuseTicks(bomb.fuse);
        });
    }

    private record CarryingBomb(Bomb bomb, int fuse, double distance) {
    }
}
