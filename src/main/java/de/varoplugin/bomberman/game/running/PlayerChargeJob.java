package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerChargeJob extends AbstractStateListenerJob {

    private final Map<Player, AbstractStateTimerJob> sneakTimers = new HashMap<>();

    public PlayerChargeJob(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(player);

        if (!bombPlayer.isAlive()) return;

        if (!event.isSneaking()) {
            AbstractStateTimerJob timer = this.sneakTimers.remove(player);
            if (timer != null) timer.stop();
            player.setExp(0);
            bombPlayer.setSneakingSince(0);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1.0f, 0.5f);
            return;
        }

        bombPlayer.setSneakingSince(System.currentTimeMillis());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);

        var job = new AbstractStateTimerJob(this.plugin, 1) {
            @Override
            public void run() {
                float vel = bombPlayer.calculateCharge();
                player.setExp((vel - 1) / (BombPlayer.MAX_CHARGE_TIME - 1));
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 0.1, 0), 5, 0.3, 0.3, 0.3, 0);
            }
        };
        this.sneakTimers.put(player, job);
        this.plugin.getHeartbeat().startJobs(job);
    }
}
