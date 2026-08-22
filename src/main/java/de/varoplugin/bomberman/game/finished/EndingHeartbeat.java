package de.varoplugin.bomberman.game.finished;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.Bukkit;

import java.util.List;

// Me I hope soon
public class EndingHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    private int count = 10;

    public EndingHeartbeat(Bomberman plugin) {
        super(plugin);
    }

    @Override
    public GameState getState() {
        return GameState.FINISHED;
    }

    @Override
    public void start() {
        super.start();

        this.plugin.getPlayers().forEach(player -> player.getPlayer().teleport(BombermanConfig.LOBBY_SPAWN.getValue()));

        List<BombPlayer> winners = this.plugin.getPlayers().filter(BombPlayer::isAlive).toList();
        if (winners.isEmpty()) {
            BombermanMessages.broadcast(BombermanMessages.GAME_END_TIE, this.plugin);
        } else {
            BombermanMessages.broadcast(BombermanMessages.GAME_END_WIN, this.plugin);
        }
    }

    @Override
    public void run() {
        if (count == 0) {
            this.plugin.getServer().shutdown();
        } else {
            BombermanMessages.broadcast(BombermanMessages.GAME_END_SHUTDOWN, this.plugin);
        }

        count--;
    }

    public int getCountdown() {
        return count;
    }
}
