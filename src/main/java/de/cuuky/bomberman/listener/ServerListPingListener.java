package de.cuuky.bomberman.listener;

import de.cuuky.bomberman.config.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = Message.MOTD.getMessage();
        if (!motd.isEmpty()) event.setMotd(motd);
    }
}