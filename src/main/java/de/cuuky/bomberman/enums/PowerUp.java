package de.cuuky.bomberman.enums;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public enum PowerUp {
    SHOCKWAVE, INFINITE_TNT, SPEED, PUNCH, REGENERATION;
    private ArrayList<Player> enabled = new ArrayList<>();

    public static PowerUp getPowerUp(Player player) {
        for (PowerUp powerup : PowerUp.values()) {
            if (!powerup.isEnabled(player)) continue;
            return powerup;
        }
        return null;
    }

    public static boolean hasPowerUp(Player player) {
        return getPowerUp(player) != null;
    }

    public static void giveAllRandomPowerUp() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getGameMode() == GameMode.SPECTATOR) continue;
            PowerUp.values()[Utils.randomInt(0, 4)].addEnabled(pl);
        }
        Bukkit.broadcastMessage(Bomberman.getPrefix() + Message.POWERUP_ALL_GAINED.getMessage());
    }

    public static void removeAllPowerUp(boolean silent) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getGameMode() == GameMode.SPECTATOR) continue;
            PowerUp pu = getPowerUp(pl);
            if (pu == null) continue;
            pu.removeEnabled(pl);
        }
        if (!silent) Bukkit.broadcastMessage(Bomberman.getPrefix() + Message.POWERUP_ALL_REMOVED.getMessage());
    }

    public boolean isEnabled(Player player) {
        return enabled.contains(player);
    }

    public ArrayList<Player> getEnabled() {
        return enabled;
    }

    public void addEnabled(Player player) {
        if (enabled.contains(player)) return;
        enabled.add(player);
        player.sendMessage(Bomberman.getPrefix() + "§7Du hast nun das Power-Up §e" + this.toString() + "§7!");
        switch (this) {
            case SHOCKWAVE:
                player.sendMessage(Bomberman.getPrefix() + "§7Damit kannst du nun mit §eSneaken §7+ §eSchlagen §7deine Gegner um dich herum wegschleudern!");
                break;
            case INFINITE_TNT:
                player.sendMessage(Bomberman.getPrefix() + "§7Damit kannst du §eunendlich TnT §7hintereinander platzieren!");
                break;
            case SPEED:
                player.sendMessage(Bomberman.getPrefix() + "§7Damit läufst du §eschneller §7als alle anderen!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 2, false, false));
                break;
            case PUNCH:
                player.sendMessage(Bomberman.getPrefix() + "§7Damit kannst du Spieler §ewegschlagen§7!");
                break;
            case REGENERATION:
                player.sendMessage(Bomberman.getPrefix() + "§7Damit §eregnerieren §7deine Wunden schnell!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100000, 2, false, false));
                break;
            default:
                break;
        }
    }

    public void removeEnabled(Player player) {
        enabled.remove(player);
        switch (this) {
            case SPEED:
                player.removePotionEffect(PotionEffectType.SPEED);
                break;
            case REGENERATION:
                player.removePotionEffect(PotionEffectType.REGENERATION);
                break;
            default:
                break;
        }
    }
}