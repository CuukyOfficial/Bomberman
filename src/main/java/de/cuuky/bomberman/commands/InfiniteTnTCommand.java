package de.cuuky.bomberman.commands;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.enums.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfiniteTnTCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Bomberman.getState() != GameState.START) {
            sender.sendMessage(Bomberman.getPrefix() + "§cDu kannst du das nur beim Start entscheiden!");
            return false;
        }
        if (Game.isUnlimitedTnTMode()) Game.setUnlimitedTnTMode(false);
        else Game.setUnlimitedTnTMode(true);
        sender.sendMessage(Bomberman.getPrefix() + "§7Der §cUnlimited-TnT-Mode §7wurde " + (Game.isUnlimitedTnTMode() ? "aktiviert" : "deaktiviert") + "!");
        return false;
    }
}