package de.cuuky.bomberman.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.enums.PowerUp;

public class ScoreboardSender {

	public static Scoreboard sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
	public static Objective obj = sb.registerNewObjective("Bomberman", "dummy");
	public static HashMap<Player, String> oldState = new HashMap<Player, String>();

	public static void sendScoreboard(Player p, int countdown) {
		sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("§eBomberman", "dummy");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		int zeit = countdown;

		int stunden = (int) zeit / 3600;
		int min = (int) (zeit - stunden * 3600) / 60;
		int sec = zeit - stunden * 3600 - min * 60;

		obj.getScore("§8").setScore(12);
		obj.getScore("§7Zeit:").setScore(11);
		if (min > 9) {
			if (sec > 9) {
				obj.getScore("§e" + min + "§7:§e" + sec).setScore(10);
			} else {
				obj.getScore("§e" + min + "§7:§e0" + sec).setScore(10);
			}
		} else {
			if (sec > 9) {
				obj.getScore("§e0" + min + "§7:§e" + sec).setScore(10);
			} else {
				obj.getScore("§e0" + min + "§7:§e0" + sec).setScore(10);
			}
		}
		obj.getScore("§7").setScore(9);
		obj.getScore("§7Spieler:").setScore(8);
		obj.getScore("§e" + Game.getAlive().size()).setScore(7);
		obj.getScore("§6").setScore(6);
		obj.getScore("§7Event:").setScore(5);
		if (Game.isUnlimitedTnTMode())
			obj.getScore("§e-").setScore(4);
		else
			obj.getScore(getEvent(getNextEvent())).setScore(4);

		obj.getScore("§3").setScore(3);

		obj.getScore("§7Power-Up§7:").setScore(2);
		if (Game.isUnlimitedTnTMode())
			obj.getScore("§cINFITE-TNT-MODE").setScore(1);
		else {
			if (PowerUp.hasPowerUp(p))
				obj.getScore("§e" + PowerUp.getPowerUp(p).toString()).setScore(1);
			else
				obj.getScore("§e-").setScore(1);
		}
		obj.getScore("§0").setScore(0);

		p.setScoreboard(sb);
	}

	public static void updateEvent() {
		String replace = getEvent(getNextEvent() + 1);
		String set = getEvent(getNextEvent());

		for (Player pl : Bukkit.getOnlinePlayers()) {
			Objective obj = pl.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

			pl.getScoreboard().resetScores(replace);
			obj.getScore(set).setScore(4);
		}
	}

	private static int getNextEvent() {
		int i = 0;
		if (Game.runningCountdown > 60)
			i++;

		if (Game.runningCountdown > 300)
			i++;

		if (Game.runningCountdown > 540)
			i++;

		return i;
	}

	private static String getEvent(int i) {
		String str = "";
		if (i == 3)
			str = "§e09:00";

		if (i == 2)
			str = "§e05:00";

		if (i == 1)
			str = "§e01:00";

		if (i == 0)
			str = "§eKein Event";

		return str;
	}

	public static void updatePowerUp(boolean remove) {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			Objective obj = pl.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
			if (obj == null)
				continue;

			PowerUp pu = PowerUp.getPowerUp(pl);

			if (remove) {
				pl.getScoreboard().resetScores("§e" + pu == null ? "-" : "§e" + pu.toString());
				obj.getScore("§e-").setScore(1);
			} else {
				pl.getScoreboard().resetScores("§e-");
				obj.getScore("§e" + pu == null ? "-" : "§e" + pu.toString()).setScore(1);
			}
		}
	}

	public static void removePlayer() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			Objective obj = pl.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

			pl.getScoreboard().resetScores("§e" + Game.getAlive().size());
			obj.getScore("§e" + (Game.getAlive().size() - 1)).setScore(7);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public static void sendLobbyScoreBoard(Player p, String state) {
		sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("§eBomberman", "dummy");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.getScore("§3").setScore(3);
		obj.getScore("§7Status:").setScore(2);
		obj.getScore(state).setScore(1);
		obj.getScore("§0").setScore(0);

		if (oldState.containsKey(oldState))
			oldState.remove(oldState);

		oldState.put(p, state);
		p.setScoreboard(sb);
	}

	@SuppressWarnings("unlikely-arg-type")
	public static void sendLobbyScore(Player p, String state) {
		if (p.getScoreboard().getEntries().contains(oldState.get(p)))
			p.getScoreboard().resetScores(oldState.get(p));

		if (oldState.containsKey(oldState))
			oldState.remove(oldState);

		oldState.put(p, state);
		p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(state).setScore(1);
	}

	public static void sendEndScoreBoard(Player p, Player winner) {
		sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("§eBomberman", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.getScore("§6").setScore(6);
		obj.getScore("§7Status:").setScore(5);
		obj.getScore("§aSpiel vorbei!").setScore(4);
		obj.getScore("§3").setScore(3);
		obj.getScore("§7Gewinner:").setScore(2);
		obj.getScore(winner == null ? "§c/" : "§e" + winner.getName()).setScore(1);
		obj.getScore("§0").setScore(0);

		p.setScoreboard(sb);
	}

	public static void sendTime(Player p, int countdown) {
		int zeit = countdown;

		int stunden = (int) zeit / 3600;
		int min = (int) (zeit - stunden * 3600) / 60;
		int sec = zeit - stunden * 3600 - min * 60;

		if (min > 9) {
			if (sec > 9) {
				p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§e" + min + "§7:§e" + sec).setScore(10);
			} else {
				p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§e" + min + "§7:§e0" + sec).setScore(10);
			}
		} else {
			if (sec > 9) {
				p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§e0" + min + "§7:§e" + sec).setScore(10);
			} else {
				p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§e0" + min + "§7:§e0" + sec).setScore(10);
			}
		}

		zeit = countdown + 1;

		stunden = (int) zeit / 3600;
		min = (int) (zeit - stunden * 3600) / 60;
		sec = zeit - stunden * 3600 - min * 60;

		if (min > 9) {
			if (sec > 9) {
				p.getScoreboard().resetScores("§e" + min + "§7:§e" + sec);
			} else {
				p.getScoreboard().resetScores("§e" + min + "§7:§e0" + sec);
			}
		} else {
			if (sec > 9) {
				p.getScoreboard().resetScores("§e0" + min + "§7:§e" + sec);
			} else {
				p.getScoreboard().resetScores("§e0" + min + "§7:§e0" + sec);
			}
		}
	}

	public static void removeScore(Player p, int countdown) {
		int zeit = countdown;

		int stunden = (int) zeit / 3600;
		int min = (int) (zeit - stunden * 3600) / 60;
		int sec = zeit - stunden * 3600 - min * 60;

		if (min > 9) {
			if (sec > 9) {
				p.getScoreboard().resetScores("§e" + min + "§7:§e" + sec);
			} else {
				p.getScoreboard().resetScores("§e" + min + "§7:§e0" + sec);
			}
		} else {
			if (sec > 9) {
				p.getScoreboard().resetScores("§e0" + min + "§7:§e" + sec);
			} else {
				p.getScoreboard().resetScores("§e0" + min + "§7:§e0" + sec);
			}
		}
	}
}