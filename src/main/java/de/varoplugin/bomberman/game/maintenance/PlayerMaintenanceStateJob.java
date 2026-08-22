package de.varoplugin.bomberman.game.maintenance;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import de.varoplugin.cfw.item.ItemBuilder;
import de.varoplugin.cfw.player.hook.item.HookItemInteractEvent;
import de.varoplugin.cfw.player.hook.item.ItemHook;
import de.varoplugin.cfw.player.hook.item.PlayerItemHookBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
