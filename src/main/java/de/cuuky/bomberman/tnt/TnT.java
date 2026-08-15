package de.cuuky.bomberman.tnt;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.enums.PowerUp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import java.util.ArrayList;

public class TnT {
    private static ArrayList<TnT> tnts = new ArrayList<>();
    private final Player shooter;
    private Player lastTouched = null;
    private final TNTPrimed primed;
    private boolean running = true;
    private int sched;
    private int level = ConfigEntry.TNT_PLACE_DELAY.getValueAsInt();

    public TnT(Player shooter, TNTPrimed primed) {
        this.shooter = shooter;
        this.primed = primed;
        startRunningSched();
        tnts.add(this);
    }

    public static TnT getTnT(Player player) {
        for (TnT tnt : tnts) {
            if (!tnt.getShooter().getName().equals(player.getName())) continue;
            return tnt;
        }
        return null;
    }

    public static TnT getTnT(Entity entity) {
        TNTPrimed tnt;
        try {
            tnt = (TNTPrimed) entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (TnT tnt1 : tnts) {
            if (tnt.getEntityId() != tnt1.getPrimedTnT().getEntityId()) continue;
            return tnt1;
        }
        return null;
    }

    public void startRunningSched() {
        if (level == 0 || Game.isUnlimitedTnTMode() || PowerUp.getPowerUp(shooter) == PowerUp.INFINITE_TNT) {
            running = false;
            return;
        }
        shooter.setLevel(level);
        sched = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bomberman.getInstance(), () -> {
            level--;
            shooter.setLevel(level);
            if (level == 0) {
                Bukkit.getScheduler().cancelTask(sched);
                running = false;
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

    public void setLastTouched(Player touched) {
        this.lastTouched = touched;
    }

    public void remove() {
        Bukkit.getScheduler().cancelTask(sched);
        tnts.remove(this);
    }
}