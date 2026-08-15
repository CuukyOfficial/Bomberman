package de.cuuky.bomberman.listener;

import de.cuuky.bomberman.base.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDroppedExp(0);
        Game.removePlayer(event.getEntity());
        event.getEntity().spigot().respawn();
        player.setGameMode(GameMode.SPECTATOR);
        event.setDeathMessage(null);
    }
}