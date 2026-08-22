package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;

public class LobbyHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    private int count = 0;

    public LobbyHeartbeat(Bomberman plugin) {
        super(plugin);

        this.registerJobs(new LobbyCancelListener(this.plugin), new NoSurvivalListener(this.plugin), new LobbyStartListener(this.plugin),
                new PlayerLobbyStateJob(this.plugin), new LobbyJoinListener(this.plugin));
    }

    @Override
    public GameState getState() {
        return GameState.LOBBY;
    }

    @Override
    public void run() {
        if (this.count >= 10) {
            BombermanMessages.broadcast(BombermanMessages.LOBBY_WAITING, this.plugin);
            this.count = 0;
        } else {
            this.count++;
        }
    }
}
