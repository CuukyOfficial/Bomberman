package de.cuuky.bomberman.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cuuky.bomberman.Bomberman;

public class SetLocCommand implements CommandExecutor {
	
	Location loc;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Bomberman.getPrefix() + "§cOnly for players!");
			return false;
		}
		
		Player player = (Player) sender;
		if (loc == null) {
			loc = player.getLocation();
			sender.sendMessage(Bomberman.getPrefix() + "§eErste Location §7gesetzt. Nochmal ausführen, um den Bereich zu sichern.");
			return false;
		}
		
		Bomberman.getBlockManager().load(loc, player.getLocation());
		loc = null;
		sender.sendMessage(Bomberman.getPrefix() + "§eBereich §7erfolgreich gesichert!");
		return false;
	}

}
