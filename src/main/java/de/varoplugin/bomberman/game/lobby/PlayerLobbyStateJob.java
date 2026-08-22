package de.varoplugin.bomberman.game.lobby;

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

public class PlayerLobbyStateJob extends AbstractStatePlayerJob {

    private final Map<Player, List<ItemHook>> hooks = new HashMap<>();

    protected PlayerLobbyStateJob(Bomberman plugin) {
        super(plugin);
    }

    @Override
    public void enable(Player player) {
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();

        ItemHook hook = new PlayerItemHookBuilder().slot(8)
                .item(ItemBuilder.itemStack(new ItemStack(Material.PAPER)).displayName("§aVote!").build())
                .subscribe(HookItemInteractEvent.class, _ ->
                        player.sendMessage("Du hast gevotet!")).complete(player, this.plugin);

        hooks.putIfAbsent(player, new ArrayList<>());
        hooks.get(player).add(hook);
    }

    @Override
    public void disable(Player player) {
        hooks.getOrDefault(player, List.of()).forEach(ItemHook::unregister);
    }
}
