package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class StartAbortListener extends AbstractStateListenerJob {

    public StartAbortListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getServer().getOnlinePlayers().size() < BombermanConfig.MIN_PAYERS.getValue()) {
            plugin.getServer().broadcastMessage("§cNicht genügend Spieler online! Das Spiel wird abgebrochen.");
            plugin.switchState(GameState.LOBBY);
        }
    }
}
