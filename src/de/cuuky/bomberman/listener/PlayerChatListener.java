package de.cuuky.bomberman.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.cuuky.bomberman.config.Message;

public class PlayerChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (Message.CHAT_FORMAT.getMessage().isEmpty())
			return;

		event.setCancelled(true);
		Bukkit.broadcastMessage(Message.CHAT_FORMAT.getMessage().replaceAll("%player%", event.getPlayer().getName())
				.replaceAll("%message%", event.getMessage()));
	}

}
