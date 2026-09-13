package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.RunnableJob;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;
import de.varoplugin.bomberman.game.running.event.PlayerPowerupChangeEvent;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;

public abstract class AbstractPowerupJob extends AbstractStateListenerJob {

    protected final PowerupEffect effect;
    private final Particle auraParticle;

    protected AbstractPowerupJob(RunningHeartbeat heartbeat, PowerupEffect effect, Particle auraParticle) {
        super(heartbeat.getPlugin());
        this.effect = effect;
        this.auraParticle = auraParticle;

        if (auraParticle != null) heartbeat.registerJobs(new RunnableJob(plugin, 5, this::spawnParticles));
    }

    private void spawnParticles() {
        this.plugin.getAlive().forEach(player -> {
            if (player.getPowerupEffect() == this.effect) {
                float strength = player.calculateCharge();
                int particleCount = (int) (4 + strength * 15);
                float radius = 1 + strength * 2;
                player.getPlayer().getWorld().spawnParticle(this.auraParticle, player.getPlayer().getLocation().add(0, 1, 0), particleCount / 2, radius / 2, radius / 2, radius / 2, 0.1);

                player.getBombs().forEach(bomb ->
                        bomb.getPrimed().getWorld().spawnParticle(this.auraParticle, bomb.getPrimed().getLocation(), particleCount, radius / 2, radius / 2, radius / 2, 0.1));
            }
        });
    }

    protected void onActivate(BombPlayer player) {
        // Default implementation does nothing
    }

    protected void onDeactivate(BombPlayer player) {
        // Default implementation does nothing
    }

    @Override
    public void stop() {
        super.stop();

        this.plugin.getAlive().forEach(player -> {
            if (player.getPowerupEffect() == this.effect) {
                this.onDeactivate(player);
            }
        });
    }

    @EventHandler
    public void onPlayerPowerupChange(PlayerPowerupChangeEvent event) {
        BombPlayer player = event.getPlayer();
        if (event.getEffect() == this.effect) {
            this.onActivate(player);
        } else if (player.getPowerupEffect() == this.effect) {
            this.onDeactivate(player);
        }
    }
}
