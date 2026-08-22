package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;

public class RunnableJob extends AbstractStateTimerJob {

    private final Runnable runnable;

    public RunnableJob(Bomberman plugin, Runnable runnable) {
        super(plugin);
        this.runnable = runnable;
    }

    public RunnableJob(Bomberman plugin, int period, Runnable runnable) {
        super(plugin, period);
        this.runnable = runnable;
    }

    public RunnableJob(Bomberman plugin, int period, boolean async, Runnable runnable) {
        super(plugin, period, async);
        this.runnable = runnable;
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}
