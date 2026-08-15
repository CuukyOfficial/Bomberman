package de.cuuky.bomberman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cuuky.bomberman.Bomberman;

public class SetSpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Bomberman.getPrefix() + "§cOnly for players!");
			return false;
		}

		Bomberman.getLocationManager().addSpawn(((Player) sender).getLocation());
		sender.sendMessage(Bomberman.getPrefix() + "§eSpawn " + Bomberman.getLocationManager().getSpawns().size()
				+ " §7erfolgreich gesetzt!");
		return false;
	}

}
