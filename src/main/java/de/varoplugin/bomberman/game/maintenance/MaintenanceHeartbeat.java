package de.varoplugin.bomberman.game.maintenance;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;

public class MaintenanceHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    public MaintenanceHeartbeat(Bomberman plugin) {
        super(plugin);

        this.registerJobs(new PlayerMaintenanceStateJob(plugin));
    }

    @Override
    public GameState getState() {
        return GameState.MAINTENANCE;
    }

    @Override
    public void run() {
        // nop
    }
}
