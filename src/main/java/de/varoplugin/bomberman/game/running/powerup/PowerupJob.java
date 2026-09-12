package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.game.RunnableJob;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;
import de.varoplugin.bomberman.model.PowerupEffect;
import de.varoplugin.bomberman.model.PowerupItem;
import de.varoplugin.cfw.world.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PowerupJob extends AbstractStateTimerJob {

    private final RunningHeartbeat heartbeat;
    private final Set<Location> possibleLocations = new HashSet<>();
    private final Map<Entity, PowerupItem> spawnedPowerups = new ConcurrentHashMap<>();

    public PowerupJob(RunningHeartbeat heartbeat) {
        super(heartbeat.getPlugin(), 20, true);
        this.heartbeat = heartbeat;

        this.heartbeat.registerJobs(new RunnableJob(this.plugin, 1, true, this::checkPlayersForCollect),
                new FreezePowerupJob(this.plugin), new CarryPowerupJob(this.plugin), new SpeedPowerupJob(this.plugin),
                new ShockwavePowerupJob(this.plugin), new DetonatorPowerupJob(this.plugin),
                new StickyPowerupJob(this.plugin),
                new BullyPowerupJob(this.plugin));
    }

    private void checkPlayersForCollect() {
        this.plugin.getAlive().forEach(player -> {
            Location playerLocation = player.getPlayer().getLocation();
            this.spawnedPowerups.forEach((crystal, powerup) -> {
                if (playerLocation.distance(crystal.getLocation()) < 1) {
                    this.plugin.getServer().getScheduler().runTask(this.plugin, powerup::remove);
                    this.spawnedPowerups.remove(crystal);
                    player.setPowerupEffect(powerup.getEffect());
                    player.getScoreboard().queueUpdate();
                }
            });
        });
    }

    private void spawnRandomPowerup() {
        if (this.possibleLocations.isEmpty()) return;

        Location location = this.possibleLocations.stream().skip((int) (Math.random() * this.possibleLocations.size())).findFirst().orElse(null);
        if (location == null) return;

        PowerupEffect effect = PowerupEffect.random();
        Hologram hologram = new Hologram(this.plugin, location.clone().add(0, 1.5, 0), effect.toString());
        Entity crystal = location.getWorld().spawnEntity(location, EntityType.END_CRYSTAL);

        PowerupItem powerup = new PowerupItem(crystal, effect, hologram);
        this.spawnedPowerups.put(crystal, powerup);
    }

    @Override
    public void run() {
        this.plugin.getAlive().forEach(player -> {
            this.possibleLocations.add(player.getPlayer().getLocation().clone());
        });

//        if (this.heartbeat.getCountdown() > BombermanConfig.GAME_LENGTH.getValue() - 30)
//            return; // Don't spawn powerups in the first 30 seconds

        long players = this.plugin.getAlive().count();
        if (Math.random() < 0.3116 * players && this.spawnedPowerups.size() < players * 2) { // Max wait time of 1 minute for per player
            this.plugin.getServer().getScheduler().runTask(this.plugin, this::spawnRandomPowerup);
        }
    }

    @Override
    public void stop() {
        super.stop();

        this.spawnedPowerups.values().forEach(PowerupItem::remove);
    }
}
