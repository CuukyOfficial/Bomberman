package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.scheduler.BukkitTask;

public class SynchronousTimerTask implements StateJob {

    protected final Bomberman plugin;
    private final Runnable runnable;
    private BukkitTask task;
    private int period = 20;

    protected SynchronousTimerTask(Bomberman plugin, Runnable runnable) {
        this.plugin = plugin;
        this.runnable = runnable;
    }

    public SynchronousTimerTask(Bomberman plugin, int period, Runnable runnable) {
        this.plugin = plugin;
        this.period = period;
        this.runnable = runnable;
    }

    protected BukkitTask createTask() {
        return this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this.runnable, 0L, this.period);
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
