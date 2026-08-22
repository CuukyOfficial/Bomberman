package de.varoplugin.bomberman.listener;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final Bomberman plugin;
    
    public PlayerListener(Bomberman plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.getPlayer(event.getPlayer());
        event.joinMessage(null);
        BombermanMessages.broadcast(BombermanMessages.PLAYER_JOIN, this.plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitLowest(PlayerQuitEvent event) {
        event.quitMessage(null);
        BombermanMessages.broadcast(BombermanMessages.PLAYER_QUIT, this.plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuitMonitor(PlayerQuitEvent event) {
        this.plugin.removePlayer(event.getPlayer());
    }
}
