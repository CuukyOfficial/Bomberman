package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.config.BombermanConfig;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.RunnableJob;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;
import de.varoplugin.bomberman.game.running.event.PlayerPowerupChangeEvent;
import de.varoplugin.bomberman.model.PowerupEffect;
import de.varoplugin.bomberman.model.PowerupItem;
import de.varoplugin.cfw.world.Hologram;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PowerupJob extends AbstractStateTimerJob {

    private static final long POWER_UP_DESPAWN_TIME = 30_000; // 30 seconds

    private final RunningHeartbeat heartbeat;
    private final Set<Location> possibleLocations = new HashSet<>();
    private final Map<Entity, PowerupItem> spawnedPowerups = new ConcurrentHashMap<>();

    public PowerupJob(RunningHeartbeat heartbeat) {
        super(heartbeat.getPlugin(), 20, true);
        this.heartbeat = heartbeat;

        this.heartbeat.registerJobs(new RunnableJob(this.plugin, 1, false, this::checkPlayersForCollect),
                new FreezePowerupJob(this.plugin), new CarryPowerupJob(this.plugin), new SpeedPowerupJob(this.plugin),
                new ShockwavePowerupJob(this.plugin), new DetonatorPowerupJob(this.plugin),
                new StickyPowerupJob(this.plugin),
                new BullyPowerupJob(this.plugin),
                new PyroPowerupJob(this.plugin));
    }

    @EventHandler
    public void onEntityDestroyed(EntityRemoveEvent event) {
        if (this.spawnedPowerups.containsKey(event.getEntity())) {
            PowerupItem powerup = this.spawnedPowerups.get(event.getEntity());
            powerup.remove();
            this.spawnedPowerups.remove(event.getEntity());
        }
    }

    private void checkPlayersForCollect() {
        this.spawnedPowerups.forEach((displayEntity, powerup) -> {
            displayEntity.setRotation(displayEntity.getLocation().getYaw() + 5f, 0);

            this.plugin.getAlive().forEach(player -> {
                Location playerLocation = player.getPlayer().getLocation();
                if (playerLocation.distance(displayEntity.getLocation()) < 1.5) {
                    this.spawnedPowerups.remove(displayEntity);
                    powerup.remove();

                    playerLocation.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, displayEntity.getLocation().add(0, 0.5, 0), 30, 0.3, 0.3, 0.3, 0.1);
                    playerLocation.getWorld().playSound(displayEntity.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);

                    PowerupEffect effect = PowerupEffect.randomExcept(player.getPowerupEffect());
                    this.plugin.getServer().getPluginManager().callEvent(new PlayerPowerupChangeEvent(player, effect));
                    player.setPowerupEffect(effect);
                    player.getScoreboard().queueUpdate();
                }
            });
        });
    }

    private void spawnRandomPowerup() {
        if (this.possibleLocations.isEmpty()) return;

        Location location = this.possibleLocations.stream().skip((int) (Math.random() * this.possibleLocations.size())).findFirst().orElse(null);
        if (location == null) return;

        String displayName = "§7§k|| §5POWER-UP §7§k||";
        Hologram hologram = new Hologram(this.plugin, location.clone().add(0, 1.2, 0), displayName);

        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0, 0.5, 0), EntityType.ITEM_DISPLAY);
        display.setItemStack(new ItemStack(Material.NETHER_STAR));

        PowerupItem powerup = new PowerupItem(display, hologram, System.currentTimeMillis());
        this.spawnedPowerups.put(display, powerup);
    }

    private Optional<Location> findItemSpawnLocationUnder(Location location) {
        Location checkLocation = location.clone();
        // Round block coordinates to the nearest integer
        checkLocation.setY(Math.floor(checkLocation.getY()) + 0.5);
        while (checkLocation.getBlockY() > 0) {
            if (checkLocation.getBlock().getType().isSolid()) {
                return Optional.of(checkLocation.add(0, 1, 0));
            }
            checkLocation.subtract(0, 1, 0);
        }
        return Optional.empty();
    }

    @Override
    public void run() {
        this.plugin.getAlive()
                .map(player -> this.findItemSpawnLocationUnder(player.getPlayer().getLocation()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(this.possibleLocations::add);

        this.spawnedPowerups.forEach((entity, powerup) -> {
            if (System.currentTimeMillis() - powerup.getSpawnTime() > POWER_UP_DESPAWN_TIME) {
                this.plugin.getServer().getScheduler().runTask(this.plugin, powerup::remove);
                this.spawnedPowerups.remove(entity);
            }
        });

        if (this.heartbeat.getCountdown() > BombermanConfig.GAME_LENGTH.getValue() - 30)
            return; // Don't spawn powerups in the first 30 seconds

        long players = this.plugin.getAlive().count();
        if (Math.random() < 0.1116 * players && this.spawnedPowerups.size() < players * 2) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, this::spawnRandomPowerup);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.spawnedPowerups.values().forEach(PowerupItem::remove);
    }
}