package de.cuuky.bomberman.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.enums.GameState;

public class StartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Bomberman.getState() != GameState.START || Game.startCountdown < 7) {
			sender.sendMessage(Bomberman.getPrefix() + "§7Das Spiel wurde bereits gestartet!");
			return false;
		}
		
		if (Bukkit.getOnlinePlayers().size() < ConfigEntry.MIN_PLAYERS.getValueAsInt()) {
			sender.sendMessage(Bomberman.getPrefix() + "§7Es sind nicht genug §eSpieler §7online!");
			return false;
		}
		
		Game.startCountdown = 6;
		sender.sendMessage(Bomberman.getPrefix() + "§7Das Spiel wurde §egestartet§7!");
		return false;
	}

}
