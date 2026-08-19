package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Job that:
 * - Enabled all player if not enabled on start
 * - Disables all player if enabled on stop
 * - Enables player on join if not enabled
 * - Disables player on quit if enabled
 * - Calls enable(player) on enable
 * - Calls disable(player) on disable
 */
public abstract class AbstractStatePlayerJob extends AbstractStateListenerJob {

    private final Set<Player> enabled;

    protected AbstractStatePlayerJob(Bomberman plugin) {
        super(plugin);

        this.enabled = new HashSet<>();
    }

    public abstract void enable(Player player);

    public abstract void disable(Player player);

    private void enableIfNotEnabled(Player player) {
        if (!this.enabled.contains(player)) {
            this.enabled.add(player);
            this.enable(player);
        }
    }

    private void disableIfEnabled(Player player) {
        if (this.enabled.contains(player)) {
            this.enabled.remove(player);
            this.disable(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.enableIfNotEnabled(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.disableIfEnabled(event.getPlayer());
    }

    @Override
    public void start() {
        super.start();

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.enableIfNotEnabled(player);
        }
    }

    @Override
    public void stop() {
        super.stop();

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.disableIfEnabled(player);
        }
    }
}
