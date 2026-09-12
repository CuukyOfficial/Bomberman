package de.varoplugin.bomberman.game.running.powerup;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateListenerJob;
import de.varoplugin.bomberman.game.running.event.PlayerThrowBombEvent;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.bomberman.model.PowerupEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSneakPowerupJob extends AbstractStateListenerJob {

    private final PowerupEffect effect;
    private final int cooldown;

    private final Map<BombPlayer, Long> cooldowns = new HashMap<>();

    protected AbstractSneakPowerupJob(Bomberman plugin, PowerupEffect effect, int cooldown) {
        super(plugin);

        this.effect = effect;
        this.cooldown = cooldown;
    }

    private boolean isCooldown(BombPlayer player) {
        long lastUsed = this.cooldowns.getOrDefault(player, 0L);
        long timeSinceLastUse = System.currentTimeMillis() - lastUsed;
        return timeSinceLastUse < this.cooldown * 1000L;
    }

    abstract void power(BombPlayer player);

    @EventHandler
    public void onPlayerPunchBomb(PlayerThrowBombEvent event) {
        BombPlayer player = event.getPlayer();
        if (player.getPowerupEffect() != this.effect || !player.getPlayer().isSneaking()) return;
        if (this.isCooldown(player)) return;

        if (this.cooldowns.getOrDefault(player, 0L) != System.currentTimeMillis()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerUsePowerup(PlayerAnimationEvent event) {
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
        BombPlayer player = this.plugin.getPlayer(event.getPlayer());
        if (!player.isAlive()) return;

        if (player.getPowerupEffect() != this.effect || !player.getPlayer().isSneaking()) return;
        if (this.isCooldown(player)) {
            player.getPlayer().sendMessage("§cYou are on cooldown for this powerup!");
            return;
        }

        this.cooldowns.put(player, System.currentTimeMillis());
        this.power(player);
    }
}