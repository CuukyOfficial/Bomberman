package de.varoplugin.bomberman.game;

import de.varoplugin.bomberman.Bomberman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class NoSurvivalListener extends AbstractStateListenerJob {

    public NoSurvivalListener(Bomberman plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        e.getWorld().setThundering(false);
        e.getWorld().setTime(6000);
        if (e.toWeatherState()) e.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setFoodLevel(40);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAchievement(PlayerAdvancementDoneEvent event) {
        event.message(null);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof LivingEntity) e.setCancelled(true);
    }
}