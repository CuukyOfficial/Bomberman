package de.cuuky.bomberman.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.enums.GameState;
import de.cuuky.bomberman.enums.PowerUp;

public class PlayerInteractListener implements Listener {

	private HashMap<Player, BukkitTask> sched = new HashMap<Player, BukkitTask>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (Bomberman.getState() == GameState.START) {
			if (event.getItem() == null)
				return;

			if (event.getItem().getItemMeta().getDisplayName() == null)
				return;
			
			if (event.getItem().getType() == Material.DIAMOND && event.getItem().getItemMeta().getDisplayName().contains("Start")) {
				player.performCommand("start");
				return;
			}

			if (event.getItem().getType() != Material.PAPER
					|| !event.getItem().getItemMeta().getDisplayName().contains("Vote"))
				return;

			player.performCommand("vote");
			return;
		} else if (Bomberman.getState() == GameState.RUNNING) {
			if (!event.getAction().equals(Action.LEFT_CLICK_AIR) && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)
					|| Bomberman.getState() != GameState.RUNNING || !player.isSneaking()
					|| player.getGameMode() != GameMode.SURVIVAL || PowerUp.getPowerUp(player) != PowerUp.SHOCKWAVE)
				return;

			if (sched.containsKey(player)) {
				player.sendMessage(
						Bomberman.getPrefix() + "§7Du kannst nur alle §e7 §7Sekunden deine §eSchockwelle §7einsetzen!");
				return;
			}

			for (Entity ent : player.getNearbyEntities(6, 6, 6))
				if (ent instanceof Player) {
					Player p = (Player) ent;
					if (!p.getGameMode().equals(GameMode.CREATIVE)) {
						p.setVelocity(p.getLocation().toVector().subtract(player.getLocation().toVector()).normalize()
								.multiply(5).setY(0.5));
						if (p.getHealth() - 6 > 0) {
							p.setHealth(p.getHealth() - 6);
						} else {
							p.setHealth(20);
							Game.removePlayer(p);
							p.getInventory().clear();
							p.setGameMode(GameMode.SPECTATOR);
							Bukkit.broadcastMessage(Message.PLAYER_DEATH_KILL_SHOCKWAVE.getMessage()
									.replaceAll("%player%", p.getName()).replaceAll("%killer%", player.getName()));
						}
					}
				} else
					ent.setVelocity(ent.getLocation().toVector().subtract(player.getLocation().toVector()).normalize()
							.multiply(5).setY(0.5));

			player.getPlayer().getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 10);
			player.sendMessage(Bomberman.getPrefix() + "§7Du hast alle Spieler in deiner Umgebung §eweggeboxt§7!");

			sched.put(player, Bukkit.getScheduler().runTaskLater(Bomberman.getInstance(), new Runnable() {

				@Override
				public void run() {
					if (player.isOnline() && PowerUp.getPowerUp(player) == PowerUp.SHOCKWAVE
							&& player.getGameMode() == GameMode.SURVIVAL)
						player.sendMessage(Bomberman.getPrefix() + "§7Deine §eSchockwelle §7ist nun aufgeladen!");

					sched.get(player).cancel();
					sched.remove(player);
				}
			}, 140));
		}
	}
}
