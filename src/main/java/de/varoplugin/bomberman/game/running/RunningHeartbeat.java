package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateHeartbeat;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RunningHeartbeat extends AbstractStateHeartbeat implements StateHeartbeat {

    public RunningHeartbeat(Bomberman plugin) {
        super(plugin);

        this.registerJobs(new BombListener(this.plugin), new NoSurvivalListener(this.plugin));
    }

    @Override
    public GameState getState() {
        return GameState.RUNNING;
    }

    @Override
    public void start() {
        super.start();

        Bukkit.broadcastMessage("§7Das Spiel hat begonnen!");
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                player.getInventory().setItem(i, new ItemStack(Material.TNT));
            }
        }
    }

    @Override
    public void run() {

    }
}
