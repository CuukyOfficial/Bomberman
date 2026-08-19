package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractStateTimerTask implements StateJob, Runnable {

    protected final Bomberman plugin;
    private BukkitTask task;
    private int period = 20;

    protected AbstractStateTimerTask(Bomberman plugin) {
        this.plugin = plugin;
    }

    protected AbstractStateTimerTask(Bomberman plugin, int period) {
        this.plugin = plugin;
        this.period = period;
    }

    protected BukkitTask createTask() {
        return this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this, 0L, this.period);
    }

    @Override
    public void start() {
        this.stop();
        this.task = this.createTask();
    }

    @Override
    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }
}
