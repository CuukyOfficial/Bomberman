package de.cuuky.bomberman.commands;

import de.cuuky.bomberman.Bomberman;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RebuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Bomberman.getBlockManager().restore();
        sender.sendMessage(Bomberman.getPrefix() + "§7Alle Blöcke wurden wieder hergestellt!");
        return false;
    }
}