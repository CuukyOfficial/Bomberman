package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.bomberman.model.BombPlayer;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BombListener extends AbstractStateListenerJob {

    private static final int MAX_TIME = 4;

    private final Map<Player, AbstractStateTimerJob> sneakTimers = new HashMap<>();

    protected BombListener(Bomberman plugin) {
        super(plugin);
    }

    private float calculateVelocity(long timeSneaking) {
        if (timeSneaking == 0) return 1;
        float x = (System.currentTimeMillis() - timeSneaking) / 1000f;
        return (float) (MAX_TIME - ((MAX_TIME - 1) * Math.exp(-0.4 * x)));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.TNT) {
            event.setCancelled(true);
            return;
        }

        BombPlayer bombPlayer = this.plugin.getPlayer(player);
        int cooldown = BombermanConfig.TNT_DELAY.getValue();
        if (bombPlayer.getBombs().anyMatch(bomb -> bomb.getPrimed().getTicksLived() < cooldown)) {
            BombermanMessages.PLAYER_COOLDOWN.send(player, player);
            event.setCancelled(true);
            return;
        }

        event.getBlock().setType(Material.AIR);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.TNT));

        Entity tntEntity = event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0, 0.5), EntityType.TNT);
        Bomb bomb = new Bomb(player, (TNTPrimed) tntEntity);
        bombPlayer.addBomb(bomb);

        player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.FLAME, tntEntity.getLocation(), 15, 0.2, 0.2, 0.2, 0.05);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(player);

        if (!bombPlayer.isAlive()) return;

        if (!event.isSneaking()) {
            AbstractStateTimerJob timer = this.sneakTimers.remove(player);
            if (timer != null) timer.stop();
            player.setExp(0);
            bombPlayer.setSneakingSince(0);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1.0f, 0.5f);
            return;
        }

        bombPlayer.setSneakingSince(System.currentTimeMillis());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);

        var job = new AbstractStateTimerJob(this.plugin, 1) {
            @Override
            public void run() {
                float vel = calculateVelocity(bombPlayer.getSneakingSince());
                player.setExp((vel - 1) / (MAX_TIME - 1));
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 0.1, 0), 5, 0.3, 0.3, 0.3, 0);
            }
        };
        this.sneakTimers.put(player, job);
        this.plugin.getHeartbeat().startJobs(job);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntityType() != EntityType.TNT) return;

        TNTPrimed tnt = (TNTPrimed) event.getEntity();
        this.plugin.removeBomb(tnt);

        event.blockList().removeIf(block -> block.getType() != Material.CLAY);
        for (Block block : event.blockList()) {
            block.setType(Material.AIR);
            block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0.5, 0.5), 5, 0.3, 0.3, 0.3, 0.05);
        }
        event.blockList().clear();

        tnt.getWorld().spawnParticle(Particle.EXPLOSION, tnt.getLocation(), 2);
        tnt.getWorld().playSound(tnt.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.8f);

        for (Entity entity : tnt.getNearbyEntities(7, 7, 7)) {
            if (entity instanceof Player player) {
                BombPlayer bombPlayer = this.plugin.getPlayer(player);
                if (!bombPlayer.isAlive()) continue;
            }

            Vector direction = entity.getLocation().toVector().subtract(tnt.getLocation().toVector()).normalize();
            entity.setVelocity(direction.multiply(2).multiply(new Vector(1, 2, 1)));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(p);
        if (!bombPlayer.isAlive()) return;

        for (TNTPrimed ent : p.getLocation().getNearbyEntitiesByType(TNTPrimed.class, 0.25, 0.25, 0.25)) {
            if (!ent.getType().equals(EntityType.TNT)) continue;
            Bomb bomb = this.plugin.getBomb(ent);
            if (bomb == null) continue;

            bomb.setLastTouched(p);
            float vel = calculateVelocity(bombPlayer.getSneakingSince());
            ent.setVelocity(new Vector(
                    p.getLocation().getDirection().multiply(vel).getX(),
                    p.getLocation().getDirection().multiply(vel).getY() + 0.5,
                    p.getLocation().getDirection().normalize().multiply(vel).getZ()
            ));
        }
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(player);
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
        if (!bombPlayer.isAlive()) return;

        RayTraceResult result = player.getWorld().rayTrace(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                4.0,
                FluidCollisionMode.NEVER,
                true,
                0.1,
                entity -> entity.getType() == EntityType.TNT
        );

        if (result != null && result.getHitEntity() != null) {
            TNTPrimed tnt = (TNTPrimed) result.getHitEntity();
            Bomb bomb = this.plugin.getBomb(tnt);

            if (bomb == null) return;
            if (tnt.getFuseTicks() == 80) return;

            bomb.setLastTouched(player);
            float vel = calculateVelocity(bombPlayer.getSneakingSince());

            Vector direction = player.getLocation().getDirection();
            Vector newVelocity = new Vector(
                    direction.getX() * vel * 1.5,
                    0.25,
                    direction.getZ() * vel * 1.5
            );

            tnt.setVelocity(newVelocity);

            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.5f);
            player.getWorld().spawnParticle(Particle.CLOUD, tnt.getLocation(), 10, 0.2, 0.2, 0.2, 0.1);
        }
    }
}