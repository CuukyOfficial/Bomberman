package de.varoplugin.bomberman.command;

import de.varoplugin.bomberman.Bomberman;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public abstract class BombermanCommand {

    private final Bomberman plugin;

    public BombermanCommand(Bomberman plugin) {
        this.plugin = plugin;
    }

    public abstract void register(Commands commands);

    public Bomberman getPlugin() {
        return plugin;
    }

    public static void register(Bomberman plugin) {
        BombermanCommand[] commands = new BombermanCommand[] {
                new MaintenanceCommand(plugin)
        };

        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            for (BombermanCommand command : commands)
                command.register(event.registrar());
        });
    }
}
