package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyAbortListener extends AbstractStateListenerJob {

    public LobbyAbortListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.plugin.getServer().getOnlinePlayers().size() - 1 < BombermanConfig.MIN_PAYERS.getValue()) {
            BombermanMessages.broadcast(BombermanMessages.LOBBY_ABORT, this.plugin);
            this.plugin.switchState(GameState.LOBBY);
        }
    }
}
