package de.cuuky.bomberman.base;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.enums.GameState;
import de.cuuky.bomberman.enums.PowerUp;
import de.cuuky.bomberman.listener.EntityDamageByEntityListener;
import de.cuuky.bomberman.scoreboard.LobbySched;
import de.cuuky.bomberman.scoreboard.ScoreboardSender;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Game {
    public static int startCountdown = 31;
    public static int runningCountdown = 601;
    public static int stoppingCountdown = 16;
    public static ArrayList<Player> votes = new ArrayList<>();
    private static boolean unlimitedTnTMode = false;
    private static ArrayList<Player> alive = new ArrayList<>();

    public Game() {
        run();
    }

    private static void shutdown() {
        for (Player pl : Bukkit.getOnlinePlayers()) pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Der Server startet jetzt neu! §aVielen Dank für's Spielen!");
        if (!ConfigEntry.RELOAD_ON_END.getValueAsBoolean()) Bukkit.getServer().shutdown();
        else {
            Bukkit.getScheduler().cancelAllTasks();
            Bukkit.getServer().reload();
        }
    }

    public static ArrayList<Player> getAlive() {
        return alive;
    }

    public static void addPlayer(Player player) {
        if (alive.contains(player)) return;
        alive.add(player);
    }

    public static void removePlayer(Player player) {
        if (!alive.contains(player)) return;
        ScoreboardSender.removePlayer();
        alive.remove(player);
        if (alive.size() == 1) Bomberman.setState(GameState.END);
    }

    public static boolean isUnlimitedTnTMode() {
        return unlimitedTnTMode;
    }

    public static void setUnlimitedTnTMode(boolean b) {
        unlimitedTnTMode = b;
    }

    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (Bomberman.getState()) {
                    case START:
                        if (Bukkit.getOnlinePlayers().size() < ConfigEntry.MIN_PLAYERS.getValueAsInt()) {
                            if (startCountdown != 31)
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Es sind jetzt zu wenig Spieler online, um das Spiel zu starten!");
                            Bukkit.getOnlinePlayers().forEach(pl -> pl.setLevel(0));
                            startCountdown = 31;
                            return;
                        }
                        if (Bukkit.getOnlinePlayers().size() == ConfigEntry.MAX_PLAYERS.getValueAsInt() && startCountdown > 6)
                            startCountdown = 6;
                        startCountdown--;
                        sendPlayerScoreBoards();
                        Bukkit.getOnlinePlayers().forEach(pl -> pl.setLevel(startCountdown));
                        if (startCountdown != 0) {
                            if (startCountdown < 6 || startCountdown == 15 || startCountdown == 10 || startCountdown == 30) {
                                Bukkit.getOnlinePlayers().forEach(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1));
                                if (startCountdown != 1)
                                    Bukkit.broadcastMessage(Bomberman.getPrefix() + "§cBomberman §7startet in §e" + startCountdown + " §7Sekunden!");
                                else
                                    Bukkit.broadcastMessage(Bomberman.getPrefix() + "§cBomberman §7startet in §eeiner §7Sekunde!");
                            }
                        } else {
                            if (votes.size() == Bukkit.getOnlinePlayers().size() || unlimitedTnTMode == true) {
                                unlimitedTnTMode = true;
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Der §cInfinite-TnT-Mode §7wurde §aaktiviert§7!");
                                for (Player pl : Bukkit.getOnlinePlayers())
                                    pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                            } else {
                                unlimitedTnTMode = false;
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Der §cInfinite-TnT-Mode §7wurde §cnicht aktiviert§7! Stimmen: §c" + votes.size() + "§7/§e" + Bukkit.getServer().getOnlinePlayers().size());
                                for (Player pl : Bukkit.getOnlinePlayers())
                                    pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                            }
                            votes.clear();
                            int i = 0;
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                addPlayer(pl);
                                pl.setGameMode(GameMode.SURVIVAL);
                                i++;
                                if (Bomberman.getLocationManager().getSpawns().get(i) == null) {
                                    Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Es nicht wurde für alle Spieler ein §eSpawn §7gefunden!");
                                    for (Player pl1 : Bukkit.getOnlinePlayers())
                                        pl1.kickPlayer(Bomberman.getPrefix() + "§7Das Spiel hat nicht für alle Spieler einen Spawn gefunden! Kehre zurück zur Lobby...");
                                    Bukkit.getServer().shutdown();
                                    break;
                                }
                                pl.getInventory().clear();
                                pl.setLevel(0);
                                pl.getInventory().setArmorContents(null);
                                pl.getInventory().setItem(0, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(1, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(2, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(3, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(4, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(5, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(6, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(7, new ItemStack(Material.TNT));
                                pl.getInventory().setItem(8, new ItemStack(Material.TNT));
                                Location loc = Bomberman.getLocationManager().getSpawns().get(i);
                                pl.teleport(loc);
                            }
                            LobbySched.cancelTask();
                            Bukkit.broadcastMessage(Bomberman.getPrefix() + "§cBomberman §7wurde §agestartet§7!");
                            Bomberman.setState(GameState.RUNNING);
                            for (Player pl : Bukkit.getOnlinePlayers())
                                pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        }
                        break;
                    case RUNNING:
                        runningCountdown--;
                        if (runningCountdown == 600) for (Player pl : Bukkit.getOnlinePlayers())
                            ScoreboardSender.sendScoreboard(pl, runningCountdown);
                        for (Player pl : Bukkit.getOnlinePlayers()) ScoreboardSender.sendTime(pl, runningCountdown);
                        if (runningCountdown == 540)
                            Bukkit.broadcastMessage(Bomberman.getPrefix() + "§aTipp: §eSneaken §7+ §egegen das TnT laufen §7schleudert das TnT noch weiter weg!");
                        if (runningCountdown == 540 || runningCountdown == 300 || runningCountdown == 60) {
                            if (unlimitedTnTMode == false) {
                                ScoreboardSender.updateEvent();
                                PowerUp.giveAllRandomPowerUp();
                                ScoreboardSender.updatePowerUp(false);
                            } else
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Aufgrund des §eInfinite-TnT-Mode's §7wurden euch §ckeine §7Effekte gegeben!");
                        }
                        if (runningCountdown == 480 || runningCountdown == 240 || runningCountdown == 0) {
                            if (unlimitedTnTMode == false) {
                                ScoreboardSender.updatePowerUp(true);
                                PowerUp.removeAllPowerUp(false);
                            }
                        }
                        if (runningCountdown == 60 || runningCountdown == 30 || runningCountdown == 15 || runningCountdown == 10 || runningCountdown < 6 && runningCountdown != 0) {
                            Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Das Spiel endet in §e" + runningCountdown + " §7Sekunden!");
                        }
                        if (runningCountdown == 0) {
                            Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Das Spiel ist vorbei!");
                            Bomberman.setState(GameState.END);
                        }
                        break;
                    case END:
                        if (Bukkit.getOnlinePlayers().size() == 0) shutdown();
                        stoppingCountdown--;
                        for (Player pl : Bukkit.getOnlinePlayers()) pl.setLevel(stoppingCountdown);
                        Player winner = getWinner();
                        if (stoppingCountdown == 15) {
                            if (winner == null)
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Niemand konnte §eBomberman §7für sich entscheiden!");
                            else
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§e" + winner.getName() + "§7 hat Bomberman für sich entschieden!");
                            Location lobby = Bomberman.getLocationManager().getLobby();
                            for (Player pl1 : Bukkit.getOnlinePlayers()) {
                                ScoreboardSender.sendEndScoreBoard(pl1, winner);
                                for (PotionEffect effect : pl1.getActivePotionEffects())
                                    pl1.removePotionEffect(effect.getType());
                                pl1.setGameMode(GameMode.ADVENTURE);
                                pl1.getInventory().clear();
                                pl1.setHealth(20);
                                if (lobby != null) pl1.teleport(lobby);
                                else
                                    pl1.sendMessage(Bomberman.getPrefix() + "§7Es wurde noch keine §eLobby §7gesetzt!");
                            }
                        }
                        if (stoppingCountdown == 15 || stoppingCountdown == 10 || stoppingCountdown < 6 && stoppingCountdown != 0) {
                            for (Player pl : Bukkit.getOnlinePlayers())
                                pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            if (stoppingCountdown != 1) {
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Der Server stoppt in §e" + stoppingCountdown + " §7Sekunden!");
                            } else {
                                Bukkit.broadcastMessage(Bomberman.getPrefix() + "§7Der Server stoppt in §eeiner §7Sekunde!");
                            }
                        }
                        if (stoppingCountdown == 0) shutdown();
                        break;
                    default:
                        break;
                }
            }
        }.runTaskTimer(Bomberman.getInstance(), 0, 20);
    }

    private void sendPlayerScoreBoards() {
        int e = startCountdown;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (e > 0) ScoreboardSender.sendLobbyScore(pl, "§aGo!");
            pl.getScoreboard().resetScores("§aWarten auf Spieler§7..");
            pl.getScoreboard().resetScores("§aWarten auf Spieler§7.");
            pl.getScoreboard().resetScores("§aWarten auf Spieler§7...");
            if (e > 3) {
                if (e == 9 || e == 5) ScoreboardSender.sendLobbyScore(pl, "§6Set...");
                if (e == 8 || e == 6 || e == 4) ScoreboardSender.sendLobbyScore(pl, "§6Set..");
                if (e == 7 || e == 3) ScoreboardSender.sendLobbyScore(pl, "§6Set.");
            }
            if (e > 9) {
                if (e == 31 || e == 30 || e == 26 || e == 22 || e == 18 || e == 14 || e == 10)
                    ScoreboardSender.sendLobbyScore(pl, "§cReady...");
                if (e == 29 || e == 27 || e == 25 || e == 23 || e == 21 || e == 19 || e == 17 || e == 15 || e == 13 || e == 11)
                    ScoreboardSender.sendLobbyScore(pl, "§cReady..");
                if (e == 28 || e == 24 || e == 20 || e == 16 || e == 12)
                    ScoreboardSender.sendLobbyScore(pl, "§cReady.");
            }
        }
    }

    private Player getWinner() {
        if (alive.size() == 1) return alive.get(0);
        else if (alive.size() == 2) {
            Player p1 = alive.get(0);
            Player p2 = alive.get(1);
            int kill1 = EntityDamageByEntityListener.kills.get(p1.getName());
            int kill2 = EntityDamageByEntityListener.kills.get(p2.getName());
            if (kill1 == kill2) return null;
            if (kill1 > kill2) return p1;
            else return p2;
        } else return alive.get(0);
    }
}