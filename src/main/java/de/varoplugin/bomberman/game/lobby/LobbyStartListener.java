package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyStartListener implements Listener {

    private final Bomberman plugin;

    public LobbyStartListener(Bomberman plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.plugin.getServer().getOnlinePlayers().size() >= 1) {
            this.plugin.getServer().broadcastMessage("§7Es sind nun §e2 §7Spieler online, das Spiel startet in §e30 Sekunden§7!");
            this.plugin.switchState(GameState.STARTING);
        }
    }
}
