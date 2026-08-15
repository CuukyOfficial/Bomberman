package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import de.varoplugin.bomberman.game.StateHeartbeat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Stream;

public class RunningHeartbeat implements StateHeartbeat {

    private final Bomberman plugin;

    public RunningHeartbeat(Bomberman plugin) {
        this.plugin = plugin;
    }

    @Override
    public Stream<Listener> createListeners() {
        return Stream.of(new BombListener(), new NoSurvivalListener());
    }

    @Override
    public void init() {
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
