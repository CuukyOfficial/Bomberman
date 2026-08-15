package de.cuuky.bomberman.listener;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.enums.GameState;
import de.cuuky.bomberman.enums.PowerUp;
import de.cuuky.bomberman.tnt.TnT;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class EntityDamageByEntityListener implements Listener {
    public static HashMap<String, Integer> kills = new HashMap<>();

    public static void playerKill(Player killer) {
        if (!kills.containsKey(killer.getUniqueId().toString())) kills.put(killer.getUniqueId().toString(), -1);
        int kill = kills.get(killer.getUniqueId().toString()) + 1;
        kills.remove(killer.getUniqueId().toString());
        kills.put(killer.getUniqueId().toString(), kill);
        if (!Message.PLAYER_TAB.getMessage().isEmpty())
            killer.setPlayerListName(Message.PLAYER_TAB.getMessage().replaceAll("%player%", killer.getName()).replaceAll("%kills%", String.valueOf(kill)));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (Bomberman.getState() != GameState.RUNNING) return;
        Player player = (Player) event.getEntity();
        if (event.getDamager() instanceof TNTPrimed) {
            TnT tnt = TnT.getTnT(event.getDamager());
            if (ConfigEntry.STARTPROTECTION.getValueAsBoolean() && Game.runningCountdown > 590) {
                player.sendMessage(Bomberman.getPrefix() + "§7Die 10 sekündige §eSchutzzeit §7hat dich vor Schaden bewahrt!");
                event.setCancelled(true);
                return;
            }
            if (player.getHealth() - event.getDamage() <= 0) {
                if (tnt.getShooter().getName().equals(player.getName())) {
                    if (tnt.getLastTouched() == null)
                        Bukkit.broadcastMessage(Message.PLAYER_DEATH_SUICIDE.getMessage().replaceAll("%player%", player.getName()));
                    else if (!tnt.getLastTouched().getName().equals(player.getName())) {
                        Bukkit.broadcastMessage(Message.PLAYER_DEATH_OWN_TNT.getMessage().replaceAll("%player%", player.getName()).replaceAll("%killer%", tnt.getLastTouched().getName()));
                        playerKill(tnt.getLastTouched());
                    }
                } else {
                    Bukkit.broadcastMessage(Message.PLAYER_DEATH_KILLER.getMessage().replaceAll("%player%", player.getName()).replaceAll("%killer%", tnt.getLastTouched().getName()));
                    playerKill(tnt.getLastTouched());
                }
                event.setCancelled(true);
                player.setHealth(20);
                player.getInventory().clear();
                Game.removePlayer(player);
                player.setGameMode(GameMode.SPECTATOR);
                player.setVelocity(tnt.getPrimedTnT().getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(2).setY(2));
            }
        } else if (event.getDamager() instanceof Player && PowerUp.getPowerUp((Player) event.getDamager()) == PowerUp.PUNCH && Bomberman.getState() == GameState.RUNNING) {
            Vector vec = event.getDamager().getLocation().getDirection();
            event.getEntity().setVelocity(new Vector(vec.multiply(2.25).getX(), 0.25, vec.multiply(2.25).getZ()));
            event.setDamage(0);
            event.setCancelled(false);
        } else event.setCancelled(true);
    }
}