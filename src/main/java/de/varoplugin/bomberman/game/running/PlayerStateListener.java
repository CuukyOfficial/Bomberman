package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PlayerType;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerStateListener extends AbstractStateListenerJob {

    protected PlayerStateListener(RunningHeartbeat heartbeat) {
        super(heartbeat.getPlugin());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        BombPlayer player = this.plugin.getPlayer(event.getPlayer());
        player.setType(PlayerType.SPECTATOR);
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
}
