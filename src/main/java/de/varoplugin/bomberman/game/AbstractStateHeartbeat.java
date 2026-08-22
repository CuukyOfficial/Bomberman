package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractStateHeartbeat extends AbstractStateTimerJob implements StateHeartbeat {

    private final List<StateJob> jobs;

    public AbstractStateHeartbeat(Bomberman plugin) {
        super(plugin);

        this.jobs = new CopyOnWriteArrayList<>();
    }

    @Override
    public void registerJobs(StateJob... jobs) {
        this.jobs.addAll(Arrays.asList(jobs));
    }

    @Override
    public void startJobs(StateJob... jobs) {
        this.registerJobs(jobs);
        Arrays.stream(jobs).forEach(StateJob::start);
    }

    @Override
    public void start() {
        super.start();

        this.jobs.forEach(StateJob::start);
    }

    @Override
    public void stop() {
        super.stop();

        this.jobs.forEach(StateJob::stop);
    }

    @Override
    public Bomberman getPlugin() {
        return this.plugin;
    }
}
