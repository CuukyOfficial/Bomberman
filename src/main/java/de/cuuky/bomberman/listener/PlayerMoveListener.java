package de.cuuky.bomberman.listener;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.enums.GameState;
import de.cuuky.bomberman.tnt.TnT;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (p.getGameMode() != GameMode.SURVIVAL) return;
        if (Bomberman.getState() != GameState.RUNNING) return;
        for (Entity ent : p.getNearbyEntities(0.25, 0.25, 0.25)) {
            if (!ent.getType().equals(EntityType.PRIMED_TNT)) continue;
            TnT tnt = TnT.getTnT(ent);
            tnt.setLastTouched(p);
            double multiply = p.isSneaking() ? 3 : 1.10;
            ent.setVelocity(new Vector(p.getLocation().getDirection().multiply(multiply).getX(), 0.25, p.getLocation().getDirection().normalize().multiply(multiply).getZ()));
        }
    }
}