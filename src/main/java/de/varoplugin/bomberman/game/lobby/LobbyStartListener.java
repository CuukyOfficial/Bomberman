package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyStartListener extends AbstractStateListenerJob {

    public LobbyStartListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.plugin.getServer().getOnlinePlayers().size() >= BombermanConfig.MIN_PAYERS.getValue()) {
            BombermanMessages.broadcast(BombermanMessages.LOBBY_STARTING);
            this.plugin.switchState(GameState.STARTING);
        }
    }
}
