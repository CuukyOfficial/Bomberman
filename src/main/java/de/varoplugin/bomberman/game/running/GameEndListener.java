package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameEndListener extends AbstractStateListenerJob {

    protected GameEndListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (BombermanConfig.MIN_PAYERS.getValue() > this.plugin.getServer().getOnlinePlayers().size()) {
            return;
        }

        this.plugin.switchState(GameState.FINISHED);
    }
}
