package de.varoplugin.bomberman.game.finished;

import de.varoplugin.bomberman.Bomberman;
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

        List<BombPlayer> winners = this.plugin.getPlayers().filter(BombPlayer::isAlive).toList();
        if (winners.isEmpty()) {
            Bukkit.broadcastMessage("§7Niemand hat gewonnen!");
        } else {
            StringBuilder message = new StringBuilder("§7Gewinner: ");
            for (int i = 0; i < winners.size(); i++) {
                BombPlayer winner = winners.get(i);
                message.append(winner.getPlayer().getName());
                if (i < winners.size() - 1) {
                    message.append(", ");
                }
            }
            Bukkit.broadcastMessage(message.toString());
        }
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
