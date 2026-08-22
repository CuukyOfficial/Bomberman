package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
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

    private final Map<Entity, Bomb> bombs = new HashMap<>();
    private final Map<Player, AbstractStateTimerJob> sneakTimers = new HashMap<>();

    protected BombListener(Bomberman plugin) {
        super(plugin);
    }

    private float calculateVelocity(long timeSneaking) {
        if (timeSneaking == 0) return 1;
        float x = (System.currentTimeMillis() - timeSneaking) / 1000f;
        return (float) (MAX_TIME - ((MAX_TIME - 1) * Math.exp(-0.4 * x)));
    }

    @Override
    public void stop() {
        this.bombs.values().forEach(bomb -> {
            bomb.getSource().setLevel(0);
            bomb.getSource().setExp(0);
            bomb.getPrimed().remove();
        });

        super.stop();
    }

    /**
     * Ausgelagerte Logik für das Schlagen von TNT.
     * So vermeiden wir doppelten Code in den verschiedenen Events.
     */
    private void punchTNT(Player player, Entity tnt) {
        Bomb bomb = this.bombs.get(tnt);
        if (bomb == null) return;

        bomb.setLastTouched(player);
        BombPlayer bombPlayer = this.plugin.getPlayer(player);
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.TNT) {
            event.setCancelled(true);
            return;
        }

        if (this.bombs.values().stream().anyMatch(bomb -> bomb.getSource().equals(player))) {
            player.sendMessage("§cDu kannst nur eine Bombe gleichzeitig platzieren!");
            event.setCancelled(true);
            return;
        }

        event.getBlock().setType(Material.AIR);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.TNT));

        Entity tntEntity = event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0, 0.5), EntityType.TNT);
        Bomb bomb = new Bomb(player, (TNTPrimed) tntEntity);
        this.bombs.put(tntEntity, bomb);

        player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.FLAME, tntEntity.getLocation(), 15, 0.2, 0.2, 0.2, 0.05);

        this.plugin.getHeartbeat().startJobs(new AbstractStateTimerJob(this.plugin, 1) {

            private Vector prevVelocity = tntEntity.getVelocity();

            @Override
            public void run() {
                if (!tntEntity.isValid() || !bombs.containsKey(tntEntity)) {
                    this.stop();
                    return;
                }

                Vector currentVelocity = tntEntity.getVelocity();
                double prevX = prevVelocity.getX();
                double currX = currentVelocity.getX();
                double prevZ = prevVelocity.getZ();
                double currZ = currentVelocity.getZ();

                boolean bounced = false;
                double bounceFactor = 0.65;

                if (Math.abs(prevX) > 0.1 && Math.abs(currX) < 0.01) {
                    currentVelocity.setX(-prevX * bounceFactor);
                    bounced = true;
                }
                if (Math.abs(prevZ) > 0.1 && Math.abs(currZ) < 0.01) {
                    currentVelocity.setZ(-prevZ * bounceFactor);
                    bounced = true;
                }

                if (bounced) {
                    tntEntity.setVelocity(currentVelocity);
                    tntEntity.getWorld().playSound(tntEntity.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.0f, 1.2f);
                }

                prevVelocity = currentVelocity.clone();
            }
        });

        this.plugin.getHeartbeat().startJobs(new AbstractStateTimerJob(this.plugin, 10) {
            @Override
            public void run() {
                if (!bombs.containsKey(tntEntity)) {
                    bomb.getSource().setLevel(0);
                    this.stop();
                } else {
                    bomb.getSource().setLevel(bomb.getRemainingSeconds());
                    tntEntity.getWorld().spawnParticle(Particle.LARGE_SMOKE, tntEntity.getLocation().add(0, 0.5, 0), 2, 0.1, 0.1, 0.1, 0.01);
                }
            }
        });
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BombPlayer bombPlayer = this.plugin.getPlayer(player);

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

        Entity tnt = event.getEntity();
        Bomb bomb = this.bombs.remove(tnt);
        if (bomb != null) {
            bomb.getSource().setLevel(0);
        }

        event.blockList().removeIf(block -> block.getType() != Material.CLAY);
        for (Block block : event.blockList()) {
            block.setType(Material.AIR);
            block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0.5, 0.5), 5, 0.3, 0.3, 0.3, 0.05);
        }
        event.blockList().clear();

        tnt.getWorld().spawnParticle(Particle.EXPLOSION, tnt.getLocation(), 2);
        tnt.getWorld().playSound(tnt.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.8f);

        for (Entity entity : tnt.getNearbyEntities(3, 3, 3)) {
            if (entity instanceof Player player) {
                BombPlayer bombPlayer = this.plugin.getPlayer(player);
                if (!bombPlayer.isAlive()) continue;
            }

            Vector direction = entity.getLocation().toVector().subtract(tnt.getLocation().toVector()).normalize();
            entity.setVelocity(direction.multiply(2).setY(0.5));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        for (Entity ent : p.getNearbyEntities(0.25, 0.25, 0.25)) {
            if (!ent.getType().equals(EntityType.TNT)) continue;
            Bomb bomb = this.bombs.get(ent);
            if (bomb == null) continue;

            bomb.setLastTouched(p);
            BombPlayer bombPlayer = this.plugin.getPlayer(p);

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
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

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
            Bomb bomb = this.bombs.get(tnt);

            if (bomb == null) return;

            if (tnt.getFuseTicks() == 80) return;

            bomb.setLastTouched(player);
            BombPlayer bombPlayer = this.plugin.getPlayer(player);
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