package de.cuuky.bomberman.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import de.cuuky.bomberman.config.Message;

public class ServerListPingListener implements Listener {

	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		String motd = Message.MOTD.getMessage();
		if (!motd.isEmpty())
			event.setMotd(motd);
	}

}
