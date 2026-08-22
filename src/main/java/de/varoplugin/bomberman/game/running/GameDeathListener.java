package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameDeathListener extends AbstractStateListenerJob {

    protected GameDeathListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        Bukkit.broadcastMessage("§e" + event.getPlayer().getName() + " §7ist gestorben!");

        BombPlayer player = this.plugin.getPlayer(event.getPlayer());
        Player p = player.getPlayer();
        player.enableSpectator(this.plugin);

        if (event.getDamageSource().getSourceLocation() != null) {
            p.setVelocity(p.getLocation().toVector().subtract(event.getDamageSource().getSourceLocation().toVector()).normalize().multiply(1.5).setY(0.5));
        } else {
            p.setVelocity(p.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.5).setY(0.5));
        }

        long aliveCount = this.plugin.getPlayers().filter(BombPlayer::isAlive).count();
        if (aliveCount > 1) {
            return;
        }

        this.plugin.switchState(GameState.FINISHED);
    }
}
