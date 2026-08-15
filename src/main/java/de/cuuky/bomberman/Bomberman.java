package de.cuuky.bomberman;

import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.block.BlockManager;
import de.cuuky.bomberman.commands.*;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.enums.GameState;
import de.cuuky.bomberman.listener.*;
import de.cuuky.bomberman.location.LocationManager;
import de.cuuky.bomberman.scoreboard.LobbySched;
import de.varoplugin.bomberman.game.NoSurvivalListener;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Bomberman extends JavaPlugin {
    private static Bomberman instance;
    private static BlockManager bm;
    private static LocationManager lm;
    private static GameState state = GameState.START;
    private static final String consolePrefix = "[Bomberman] ";

    public static GameState getState() {
        return state;
    }

    public static void setState(GameState state) {
        Bomberman.state = state;
    }

    public static Bomberman getInstance() {
        return instance;
    }

    public static LocationManager getLocationManager() {
        return lm;
    }

    public static BlockManager getBlockManager() {
        return bm;
    }

    public static String getPrefix() {
        return ConfigEntry.PREFIX.getValueAsString().replaceAll("&", "§");
    }

    @Override
    public void onEnable() {
        System.out.println("-------------------------------------------");
        System.out.println(consolePrefix + "Enabling Bomberman...");
        instance = this;
        ConfigEntry.loadAll();
        Message.loadAll();
        System.out.println(consolePrefix + "Loaded configuration!");
        Bukkit.getWorlds().forEach(world -> world.setDifficulty(Difficulty.EASY));
        Bukkit.getServer().setSpawnRadius(0);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            PlayerJoinListener.setTab(pl);
            PlayerJoinListener.giveItems(pl);
            pl.setGameMode(GameMode.ADVENTURE);
        }
        registerCommands();
        registerListener();
        new LobbySched();
        new Game();
        System.out.println(consolePrefix + "Loading blocks and locations...");
        bm = new BlockManager();
        bm.restore();
        lm = new LocationManager();
        System.out.println(consolePrefix + "Bomberman successfully enabled!");
        System.out.println("-------------------------------------------");
    }

    @Override
    public void onDisable() {
        System.out.println("-------------------------------------------");
        System.out.println(consolePrefix + "Disabling Bomberman...");
        System.out.println(consolePrefix + "Restoring clay blocks...");
        bm.restore();
        System.out.println(consolePrefix + "Saving files...");
        bm.save();
        lm.save();
        for (Player pl : Bukkit.getOnlinePlayers()) pl.kickPlayer("§cServer startet jetzt neu...");
        System.out.println(consolePrefix + "Bomberman successfully disabled!");
        System.out.println("-------------------------------------------");
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new NoSurvivalListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new ServerListPingListener(), this);
    }

    public void registerCommands() {
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("infinitetnt").setExecutor(new InfiniteTnTCommand());
        getCommand("rebuild").setExecutor(new RebuildCommand());
        getCommand("setLobby").setExecutor(new SetLobbyCommand());
        getCommand("setSpawn").setExecutor(new SetSpawnCommand());
        getCommand("setLoc").setExecutor(new SetLocCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("vote").setExecutor(new VoteCommand());
    }
}