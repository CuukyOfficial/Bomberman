package de.cuuky.bomberman.commands;

import de.cuuky.bomberman.Bomberman;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Bomberman.getPrefix() + "§cOnly for players!");
            return false;
        }
        Bomberman.getLocationManager().setLobby(((Player) sender).getLocation());
        sender.sendMessage(Bomberman.getPrefix() + "§eLobby §7erfolgreich gesetzt!");
        return false;
    }
}