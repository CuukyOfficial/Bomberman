package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractStateTimerJob implements StateJob, Runnable {

    protected final Bomberman plugin;
    private BukkitTask task;
    private int period = 20;
    private boolean async = false;

    protected AbstractStateTimerJob(Bomberman plugin) {
        this.plugin = plugin;
    }

    protected AbstractStateTimerJob(Bomberman plugin, int period) {
        this.plugin = plugin;
        this.period = period;
    }

    protected AbstractStateTimerJob(Bomberman plugin, int period, boolean async) {
        this.plugin = plugin;
        this.period = period;
        this.async = async;
    }

    protected BukkitTask createTask() {
        if (this.async) {
            return this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, this, 0L, this.period);
        } else {
            return this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this, 0L, this.period);
        }
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
