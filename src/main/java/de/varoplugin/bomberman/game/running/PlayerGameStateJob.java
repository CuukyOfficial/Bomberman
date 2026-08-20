package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerGameStateJob extends AbstractStatePlayerJob {

    private RunningHeartbeat heartbeat;

    protected PlayerGameStateJob(RunningHeartbeat heartbeat) {
        super(heartbeat.getPlugin());
    }

    @Override
    public void enable(Player player) {
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i, new ItemStack(Material.TNT));
        }

        this.heartbeat.addPlayer(player);
    }

    @Override
    public void disable(Player player) {
    }
}
