package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyJoinListener extends AbstractStateListenerJob {

    public LobbyJoinListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(BombermanConfig.LOBBY_SPAWN.getValue());
    }
}
