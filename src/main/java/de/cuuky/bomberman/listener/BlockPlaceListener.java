package de.cuuky.bomberman.listener;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.commands.BuildCommand;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.tnt.TnT;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.TNT) {
            TnT tnt = TnT.getTnT(event.getPlayer());
            if (tnt != null) if (tnt.isRunning()) {
                player.sendMessage(Bomberman.getPrefix() + Message.TNT_SET_DELAY.getMessage().replaceAll("%seconds%", String.valueOf(ConfigEntry.TNT_PLACE_DELAY.getValueAsInt())));
                event.setCancelled(true);
                return;
            }
            event.getBlock().setType(Material.AIR);
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.TNT));
            new TnT(event.getPlayer(), (TNTPrimed) event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.TNT));
            return;
        } else if (BuildCommand.buildMode.contains(player.getName())) return;
        else event.setCancelled(true);
    }
}