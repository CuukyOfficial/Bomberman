package de.varoplugin.bomberman.game.lobby;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerLobbyStateJob extends AbstractStatePlayerJob {

    protected PlayerLobbyStateJob(Bomberman plugin) {
        super(plugin);
    }

    @Override
    public void enable(Player player) {
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
    }

    @Override
    public void disable(Player player) {
    }
}
