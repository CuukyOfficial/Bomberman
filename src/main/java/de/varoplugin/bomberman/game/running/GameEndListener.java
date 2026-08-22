package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameEndListener extends AbstractStateListenerJob {

    protected GameEndListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        long aliveCount = this.plugin.getPlayers().filter(BombPlayer::isAlive).count();
        if (BombermanConfig.MIN_PAYERS.getValue() > aliveCount) {
            return;
        }

        this.plugin.switchState(GameState.FINISHED);
    }
}
