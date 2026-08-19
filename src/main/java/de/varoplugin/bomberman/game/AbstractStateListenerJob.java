package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class AbstractStateListenerJob implements StateJob, Listener {

    protected final Bomberman plugin;

    protected AbstractStateListenerJob(Bomberman plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }
}
