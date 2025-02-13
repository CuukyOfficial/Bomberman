package de.cuuky.bomberman.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.config.ConfigEntry;

public class LobbySched {

	private static int i = 0;

	public LobbySched() {
		i = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bomberman.getInstance(), new Runnable() {

			int e = 20;

			@Override
			public void run() {
				try {
					if (Bukkit.getOnlinePlayers().size() != 0 && Bukkit.getOnlinePlayers().size() < ConfigEntry.MIN_PLAYERS.getValueAsInt()) {
						e--;
						if (e == 0) {
							Bukkit.broadcastMessage(
									Bomberman.getPrefix() + "§7Es werden §e2 §7Spieler benötigt, um §eBomberman §7zu starten!");
							e = 20;
						}

						if (e == 20 || e == 16 || e == 12 || e == 8 || e == 4 || e == 0)
							for (Player pl : Bukkit.getOnlinePlayers())
								ScoreboardSender.sendLobbyScore(pl, "§aWarten auf Spieler§7...");

						if (e == 19 || e == 17 || e == 15 || e == 13 || e == 11 || e == 9 || e == 7 || e == 5 || e == 3
								|| e == 1)
							for (Player pl : Bukkit.getOnlinePlayers())
								ScoreboardSender.sendLobbyScore(pl, "§aWarten auf Spieler§7..");

						if (e == 18 || e == 14 || e == 10 || e == 6 || e == 2)
							for (Player pl : Bukkit.getOnlinePlayers())
								ScoreboardSender.sendLobbyScore(pl, "§aWarten auf Spieler§7.");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}, 20L, 20L);
	}
	
	public static void cancelTask() {
		Bukkit.getScheduler().cancelTask(i);
	}
}
