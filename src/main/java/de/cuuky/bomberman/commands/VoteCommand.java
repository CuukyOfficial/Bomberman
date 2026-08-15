package de.cuuky.bomberman.commands;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Bomberman.getPrefix() + "§7Nicht für die Konsole!");
            return false;
        }
        if (Bomberman.getState() != GameState.START) {
            sender.sendMessage(Bomberman.getPrefix() + "§7Spiel ist bereits gestartet!");
            return false;
        }
        if (!ConfigEntry.ALLOW_INFINITE_TNT_MODE.getValueAsBoolean()) {
            sender.sendMessage(Bomberman.getPrefix() + "§7Der §cInfinite-TnT-Mode §7wurde in der Config deaktiviert!");
            return false;
        }
        Player p = (Player) sender;
        if (Game.votes.contains(p)) {
            Game.votes.remove(p);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
            p.sendMessage(Bomberman.getPrefix() + "§7Du hast §cgegen §7den §cInfinite-TnT-Mode §7gestimmt!");
        } else {
            Game.votes.add(p);
            p.sendMessage(Bomberman.getPrefix() + "§7Du hast §afür §7den §cInfinite-TnT-Mode §7gestimmt!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }
        p.sendMessage(Bomberman.getPrefix() + "§7Momentan sind §e" + Game.votes.size() + " §7von §e" + Bukkit.getServer().getOnlinePlayers().size() + " §7Spieler für den §cInfinite-TnT-Mode§7!");
        return false;
    }
}