package de.varoplugin.bomberman.game.running.events;

import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;

public abstract class BombermanEvent extends AbstractStateListenerJob {

    private final RunningHeartbeat heartbeat;
    private final String name;

    public BombermanEvent(RunningHeartbeat heartbeat, String name) {
        super(heartbeat.getPlugin());
        this.heartbeat = heartbeat;
        this.name = name;
    }

    public void run(int countdown) {}

    @Override
    public void start() {
        super.start();

        this.heartbeat.setEvent(this);
    }

    @Override
    public void stop() {
        super.stop();

        this.heartbeat.setEvent(null);
    }

    public RunningHeartbeat getHeartbeat() {
        return heartbeat;
    }

    public String getName() {
        return name;
    }
}
