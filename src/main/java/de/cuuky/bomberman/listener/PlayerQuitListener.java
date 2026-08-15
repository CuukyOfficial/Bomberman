package de.cuuky.bomberman.listener;

import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Message.PLAYER_QUIT.getMessage().replaceAll("%player%", event.getPlayer().getName()));
        Player player = event.getPlayer();
        if (EntityDamageByEntityListener.kills.containsKey(player.getUniqueId().toString()))
            EntityDamageByEntityListener.kills.remove(player.getUniqueId().toString());
        Game.votes.remove(player);
        Game.removePlayer(player);
    }
}