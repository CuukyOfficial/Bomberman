package de.varoplugin.bomberman.game.finished;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.StateHeartbeat;
import de.varoplugin.bomberman.hud.ScoreboardListener;
import org.bukkit.Bukkit;

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

        Bukkit.broadcastMessage("§7Das Spiel ist vorbei!");
    }

    @Override
    public void run() {
        if (count == 0) {
            this.plugin.getServer().shutdown();
        } else {
            Bukkit.broadcastMessage("§7Spiel endet in " + count + " Sekunden");
        }

        count--;
    }
}
