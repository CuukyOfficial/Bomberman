package de.varoplugin.bomberman.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.BombermanPermissions;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.GameState;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class MaintenanceCommand extends BombermanCommand {

    public MaintenanceCommand(Bomberman plugin) {
        super(plugin);
    }

    @Override
    public void register(Commands commands) {
        commands.register(Commands.literal("maintenance").requires(source -> source.getSender()
                .hasPermission(BombermanPermissions.MAINTENANCE)).executes(this::execute).build());
    }

    public int execute(CommandContext<CommandSourceStack> context) {
        if (this.getPlugin().getHeartbeat().getState() == GameState.MAINTENANCE) {
            this.getPlugin().switchState(GameState.LOBBY);
            BombermanMessages.broadcast(BombermanMessages.COMMAND_MAINTENANCE_DISABLED, this.getPlugin());
        } else {
            this.getPlugin().switchState(GameState.MAINTENANCE);
            BombermanMessages.broadcast(BombermanMessages.COMMAND_MAINTENANCE_ENABLED, this.getPlugin());
        }
        return Command.SINGLE_SUCCESS;
    }
}
