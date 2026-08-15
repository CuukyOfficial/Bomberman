package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class StartAbortListener implements Listener {

    private final Bomberman plugin;

    public StartAbortListener(Bomberman plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getServer().getOnlinePlayers().size() < 2) {
            plugin.getServer().broadcastMessage("§cNicht genügend Spieler online! Das Spiel wird abgebrochen.");
            plugin.switchState(GameState.LOBBY);
        }
    }
}
