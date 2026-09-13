package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;
import de.varoplugin.bomberman.game.running.event.PlayerThrowBombEvent;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

public abstract class AbstractSneakPowerupJob extends AbstractPowerupJob {

    private final int cooldown;

    protected AbstractSneakPowerupJob(RunningHeartbeat heartbeat, PowerupEffect effect, Particle auraParticle, int cooldown) {
        super(heartbeat, effect, auraParticle);

        this.cooldown = cooldown;
    }

    private boolean isCooldown(BombPlayer player) {
        return player.getPowerUp().calculateCooldownRemaining() > 0;
    }

    abstract boolean power(BombPlayer player);

    @EventHandler
    public void onPlayerPunchBomb(PlayerThrowBombEvent event) {
        BombPlayer player = event.getPlayer();
        if (player.getPowerupEffect() != this.effect || !player.getPlayer().isSneaking()) return;
        if (this.isCooldown(player)) return;

        if (player.getPowerUp().calculateCooldownRemaining() == this.cooldown * 1000L) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUsePowerup(PlayerAnimationEvent event) {
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
        BombPlayer player = this.plugin.getPlayer(event.getPlayer());
        if (!player.isAlive()) return;

        if (player.getPowerupEffect() != this.effect || !player.getPlayer().isSneaking()) return;
        if (this.isCooldown(player)) {
            player.getPlayer().sendMessage(BombermanMessages.GAME_PLAYER_COOLDOWN.stringValue(player));
            return;
        }

        if (this.power(player)) {
            player.getPowerUp().setCooldownUntil(System.currentTimeMillis() + this.cooldown * 1000L);
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
        }
    }
}