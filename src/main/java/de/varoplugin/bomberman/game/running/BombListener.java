package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerTask;
import de.varoplugin.bomberman.model.Bomb;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BombListener extends AbstractStateListenerTask {

    private final Map<Entity, Bomb> bombs = new HashMap<>();

    protected BombListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.TNT) {
//            TnT tnt = TnT.getTnT(event.getPlayer());
//            if (tnt != null) if (tnt.isRunning()) {
//                player.sendMessage(Bomberman.getPrefix() + Message.TNT_SET_DELAY.getMessage().replaceAll("%seconds%", String.valueOf(ConfigEntry.TNT_PLACE_DELAY.getValueAsInt())));
//                event.setCancelled(true);
//                return;
//            } TODO: Add delay for placing bombs
            event.getBlock().setType(Material.AIR);
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.TNT));
            Entity tntEntity = event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.TNT);
            bombs.put(tntEntity, new Bomb(event.getPlayer(), (TNTPrimed) tntEntity));
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (!e.getEntity().getType().equals(EntityType.TNT)) return;
        bombs.remove(e.getEntity());
        Iterator<Block> iter = e.blockList().iterator();
        while (iter.hasNext()) {
            Block b = iter.next();
            if (!b.getType().equals(Material.CLAY)) iter.remove();
            else {
                b.setType(Material.AIR);
                b.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        for (Entity ent : p.getNearbyEntities(0.25, 0.25, 0.25)) {
            if (!ent.getType().equals(EntityType.TNT)) continue;
            Bomb bomb = this.bombs.get(ent);
            bomb.setLastTouched(p);
            double multiply = p.isSneaking() ? 3 : 1.10;
            ent.setVelocity(new Vector(p.getLocation().getDirection().multiply(multiply).getX(), 0.25, p.getLocation().getDirection().normalize().multiply(multiply).getZ()));
        }
    }
}
