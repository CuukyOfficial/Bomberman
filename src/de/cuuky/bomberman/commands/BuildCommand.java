package de.cuuky.bomberman.commands;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.listener.PlayerJoinListener;

public class BuildCommand implements CommandExecutor {

	public static ArrayList<String> buildMode = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Bomberman.getPrefix() + "Not for Console!");
				return false;
			}

			Player player = (Player) sender;
			if (buildMode.contains(sender.getName())) {
				buildMode.remove(sender.getName());
				player.setGameMode(GameMode.SURVIVAL);
				PlayerJoinListener.giveItems(player);
				sender.sendMessage(Bomberman.getPrefix() + "Du bist jetzt §cnicht §7mehr im Build-Modus!");
			} else {
				buildMode.add(sender.getName());
				player.setGameMode(GameMode.CREATIVE);
				player.getInventory().clear();
				sender.sendMessage(Bomberman.getPrefix() + "Du bist jetzt im §2Build§7-Modus!");
			}
			return false;
		} else if (args.length == 1) {

		} else
			sender.sendMessage(Bomberman.getPrefix() + "§c/build");
		return false;
	}
}
