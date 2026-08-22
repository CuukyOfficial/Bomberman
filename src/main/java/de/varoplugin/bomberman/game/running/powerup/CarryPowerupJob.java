package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class CarryPowerupJob extends AbstractStateTimerJob {

    private final Map<BombPlayer, Bomb> carryingPlayers = new HashMap<>();

    public CarryPowerupJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(player);
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
        if (!bombPlayer.isAlive()) return;

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
            this.carryingPlayers.put(bombPlayer, bomb);
        }
    }

    @Override
    public void run() {
        // make that tnt follows the player wherever je walks or looks with the tnt and freezes the ticks until he is done
        this.carryingPlayers.forEach((player, bomb) -> {
            // Update the position of the bomb to follow the player where he looks

        });
    }
}
