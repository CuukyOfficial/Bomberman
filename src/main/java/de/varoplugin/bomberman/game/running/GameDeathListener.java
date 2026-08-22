package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PlayerType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class GameDeathListener extends AbstractStateListenerJob {

    protected GameDeathListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setCancelled(true);

        BombPlayer player = this.plugin.getPlayer(event.getPlayer());
        player.setType(PlayerType.SPECTATOR);

        Player p = event.getPlayer();
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setHealth(20);
        p.getInventory().clear();
        p.setVelocity(p.getLocation().toVector().subtract(Objects.requireNonNull(event.getDamageSource().getSourceLocation()).toVector()).normalize().multiply(5).setY(0.5));
        p.setFallDistance(0);
        p.setNoDamageTicks(0);
        p.setFireTicks(0);
        p.setInvulnerable(false);

        long aliveCount = this.plugin.getPlayers().filter(BombPlayer::isAlive).count();
        if (aliveCount > 1) {
            return;
        }

        this.plugin.switchState(GameState.FINISHED);
    }
}
