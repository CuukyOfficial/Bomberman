package de.cuuky.bomberman.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.enums.GameState;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (Bomberman.getState() != GameState.RUNNING)
			event.setCancelled(true);
		else if (event.getEntity() instanceof Player) {
			if (Bomberman.getState() != GameState.RUNNING)
				return;
			
			Player p = (Player) event.getEntity();
			if (!(p.getHealth() - event.getDamage() <= 0))
				return;
			
			if (!Game.getAlive().contains(p))
				return;

			event.setCancelled(true);
			p.setHealth(20);
			Game.removePlayer(p);
			p.getInventory().clear();
			p.setGameMode(GameMode.SPECTATOR);
			Bukkit.broadcastMessage(Message.PLAYER_DEATH.getMessage().replaceAll("%player%", p.getName()));
		}
	}
}
