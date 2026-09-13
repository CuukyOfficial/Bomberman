package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.running.event.PlayerThrowBombEvent;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.util.RayTraceResult;

import java.util.HashSet;
import java.util.Set;

public class DetonatorPowerupJob extends AbstractSneakPowerupJob {

    protected DetonatorPowerupJob(Bomberman plugin) {
        super(plugin, PowerupEffect.DETONATOR, 6);
    }

    @EventHandler
    public void onPlayerThrowBomb(PlayerThrowBombEvent event) {
        BombPlayer player = event.getPlayer();
        if (player.getPowerupEffect() != PowerupEffect.DETONATOR || !player.getPlayer().isSneaking()) return;

        event.getBomb().getPrimed().setFuseTicks(80);
    }

    @Override
    void power(BombPlayer player) {
        Player p = player.getPlayer();
        float strength = player.calculateCharge();
        double distance = 5 + strength * 10;
        int amountOfBombs = (int) (1 + strength * 3);
        // Cast a ray where the player is looking with the distance depending on the strength of the powerup and detonate all bombs where the player is looking at
        Set<TNTPrimed> toDetonate = new HashSet<>();

        for (int i = 0; i < amountOfBombs; i++) {
            RayTraceResult result = p.getWorld().rayTrace(
                    p.getEyeLocation(),
                    p.getEyeLocation().getDirection(),
                    distance,
                    FluidCollisionMode.NEVER,
                    true,
                    0.1,
                    entity -> entity.getType() == EntityType.TNT && this.plugin.getBomb((TNTPrimed) entity).getSource() == player && !toDetonate.contains((TNTPrimed) entity)
            );

            if (result == null || result.getHitEntity() == null) {
                break;
            }

            toDetonate.add((TNTPrimed) result.getHitEntity());
        }

        for (TNTPrimed tntPrimed : toDetonate) {
            tntPrimed.setFuseTicks(0);
        }
    }
}
