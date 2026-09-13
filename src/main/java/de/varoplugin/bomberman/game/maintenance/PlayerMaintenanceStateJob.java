package de.varoplugin.bomberman.game.maintenance;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerMaintenanceStateJob extends AbstractStatePlayerJob {

    protected PlayerMaintenanceStateJob(Bomberman plugin) {
        super(plugin);
    }

    @Override
    public void enable(Player player) {
        player.setGameMode(GameMode.CREATIVE);
    }

    @Override
    public void disable(Player player) {
        // nop
    }
}
