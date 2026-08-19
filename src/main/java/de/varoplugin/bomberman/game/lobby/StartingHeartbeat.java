package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import org.bukkit.Bukkit;

public class StartingHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    private int countdown;

    public StartingHeartbeat(Bomberman plugin) {
        super(plugin);
        this.countdown = BombermanConfig.LOBBY_DELAY.getValue();

        this.registerJobs(new StartAbortListener(this.plugin), new LobbyCancelListener(this.plugin));
    }

    @Override
    public GameState getState() {
        return GameState.STARTING;
    }

    @Override
    public void run() {
        if (this.countdown == 0) {
            this.plugin.switchState(GameState.RUNNING);
            return;
        }

        if (this.countdown % 5 == 0 || this.countdown <= 5) {
            Bukkit.broadcastMessage("Das Spiel startet in " + this.countdown + " Sekunden!");
        }

        this.countdown--;
    }
}
