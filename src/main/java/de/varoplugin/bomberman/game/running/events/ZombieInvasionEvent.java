package de.varoplugin.bomberman.game.running.events;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.running.RunningHeartbeat;
import de.varoplugin.cfw.player.hook.item.PlayerItemHook;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ZombieInvasionEvent extends BombermanEvent {

    private static final float EXPLOSION_POWER = 6F; // TNT is 4
    private static final int EXPLOSION_DELAY = 20;

    private final List<Zombie> zombies = new ArrayList<>();
    private int start_time;

    public ZombieInvasionEvent(RunningHeartbeat heartbeat) {
        super(heartbeat, "Zombie Invasion");
    }

    @Override
    public void start() {
        super.start();

        this.start_time = this.getHeartbeat().getCountdown();
        this.plugin.getAlive().forEach(player -> {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 1.0f);

            Zombie entity = (Zombie) player.getPlayer().getLocation().getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.ZOMBIE);
            entity.setShouldBurnInDay(false);
            entity.setInvulnerable(true);
            entity.setTarget(player.getPlayer());
            entity.getEquipment().setHelmet(new ItemStack(Material.TNT), true);
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 5, false, false, false));
            entity.setInvisible(false);

            player.getPlayer().showEntity(this.plugin, entity);

            this.zombies.add(entity);
        });
    }

    @Override
    public void stop() {
        super.stop();

        for (Zombie zombie : this.zombies) {
            zombie.getWorld().createExplosion(zombie, EXPLOSION_POWER, false, false);
            zombie.remove();
        }
        this.zombies.clear();
    }

    @Override
    public void run(int countdown) {
        this.plugin.getAlive().forEach(player -> {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
        });

        if (this.start_time - countdown == EXPLOSION_DELAY)
            this.stop();
    }
}
