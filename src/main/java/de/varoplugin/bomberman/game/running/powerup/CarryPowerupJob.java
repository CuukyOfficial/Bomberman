package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CarryPowerupJob extends AbstractStateTimerJob {

    private final Map<BombPlayer, CarryingBomb> carryingPlayers = new ConcurrentHashMap<>();

    public CarryPowerupJob(Bomberman plugin) {
        super(plugin, 1, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(player);
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
        if (!bombPlayer.isAlive()) return;
        if (bombPlayer.getPowerupEffect() != PowerupEffect.CARRY) return;

        CarryingBomb exist = this.carryingPlayers.get(bombPlayer);
        if (exist != null) {
            this.carryingPlayers.remove(bombPlayer);
            return;
        }

        // Async schon?
        RayTraceResult result = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                4.0,
                FluidCollisionMode.NEVER,
                true,
                0.1,
                entity -> entity.getType() == EntityType.TNT
        );

        // Async
        if (result != null && result.getHitEntity() != null) {
            TNTPrimed tnt = (TNTPrimed) result.getHitEntity();
            Bomb bomb = this.plugin.getBomb(tnt);

            if (bomb == null) return;
            if (tnt.getFuseTicks() == 80) return;

            bomb.setLastTouched(player);
            // Cancel vel from previous
            tnt.setVelocity(new Vector(0, 0, 0));
            double distance = tnt.getLocation().distance(player.getEyeLocation());
            this.carryingPlayers.put(bombPlayer, new CarryingBomb(bomb, tnt.getFuseTicks(), distance));
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
