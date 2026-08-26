package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractStateTimerJob extends AbstractStateListenerJob implements StateJob, Runnable {

    private BukkitTask task;
    private int period = 20;
    private boolean async = false;

    protected AbstractStateTimerJob(Bomberman plugin) {
        super(plugin);
    }

    protected AbstractStateTimerJob(Bomberman plugin, int period) {
        this(plugin);
        this.period = period;
    }

    protected AbstractStateTimerJob(Bomberman plugin, int period, boolean async) {
        this(plugin, period);
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
        super.start();

        if (this.task != null)
            throw new IllegalStateException("Task " + this + " already started");

        this.task = this.createTask();
    }

    @Override
    public void stop() {
        super.stop();

        if (this.task != null) {
            this.task.cancel();
        }
    }
}
