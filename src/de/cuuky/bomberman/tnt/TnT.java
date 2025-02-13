package de.cuuky.bomberman.tnt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.enums.PowerUp;

public class TnT {

	private static ArrayList<TnT> tnts = new ArrayList<>();

	private Player shooter;
	private Player lastTouched = null;
	private TNTPrimed primed;
	private boolean running = true;
	private int sched;
	private int level = ConfigEntry.TNT_PLACE_DELAY.getValueAsInt();

	public TnT(Player shooter, TNTPrimed primed) {
		this.shooter = shooter;
		this.primed = primed;

		startRunningSched();

		tnts.add(this);
	}

	public void startRunningSched() {
		if (level == 0 || Game.isUnlimitedTnTMode() || PowerUp.getPowerUp(shooter) == PowerUp.INFINITE_TNT) {
			running = false;
			return;
		}

		shooter.setLevel(level);
		sched = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bomberman.getInstance(), new Runnable() {

			@Override
			public void run() {
				level--;
				shooter.setLevel(level);

				if (level == 0) {
					Bukkit.getScheduler().cancelTask(sched);
					running = false;
					return;
				}
			}
		}, 20, 20);
	}

	public TNTPrimed getPrimedTnT() {
		return this.primed;
	}

	public boolean isRunning() {
		return running;
	}

	public Player getShooter() {
		return this.shooter;
	}

	public Player getLastTouched() {
		return lastTouched;
	}

	public void remove() {
		Bukkit.getScheduler().cancelTask(sched);
		tnts.remove(this);
	}

	public void setLastTouched(Player touched) {
		this.lastTouched = touched;
	}

	public static TnT getTnT(Player player) {
		for (TnT tnt : tnts) {
			if (!tnt.getShooter().getName().equals(player.getName()))
				continue;

			return tnt;
		}

		return null;
	}

	public static TnT getTnT(Entity entity) {
		TNTPrimed tnt = null;
		try {
			tnt = (TNTPrimed) entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		for (TnT tnt1 : tnts) {
			if (tnt.getEntityId() != tnt1.getPrimedTnT().getEntityId())
				continue;

			return tnt1;
		}

		return null;
	}
}
