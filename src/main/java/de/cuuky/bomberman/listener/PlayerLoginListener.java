package de.cuuky.bomberman.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.enums.GameState;

public class PlayerLoginListener implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (Bomberman.getState() == GameState.END)
			event.disallow(Result.KICK_OTHER, "§cDas Spiel ist bereits vorbei!");

		if (Bomberman.getState() == GameState.START
				&& Bukkit.getOnlinePlayers().size() >= ConfigEntry.MAX_PLAYERS.getValueAsInt())
			event.disallow(Result.KICK_FULL,
					"§cDas Spiel ist bereits voll! §7Warte kurz, dann kannst du als Spectator joinen.");

		if (Bomberman.getState() == GameState.RUNNING && !ConfigEntry.ALLOW_SPECTATORS.getValueAsBoolean())
			event.disallow(Result.KICK_OTHER, "§cDas Spectating wurde in der Config deaktiviert!");
	}

}
