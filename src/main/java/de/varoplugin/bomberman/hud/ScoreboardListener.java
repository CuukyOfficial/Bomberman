package de.varoplugin.bomberman.hud;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import de.varoplugin.cfw.player.hud.AnimationData;
import de.varoplugin.cfw.player.hud.ScoreboardInstance;
import de.varoplugin.cfw.player.hud.UnmodifiableAnimationData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardListener implements Listener {
    
    private static final int SCOREBOARD_UPDATE_DELAY = 20;

    private final Bomberman plugin;
    private final Map<Player, AnimatedScoreboard> scoreboards = new HashMap<>();

    public ScoreboardListener(Bomberman plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ScoreboardInstance instance = ScoreboardInstance.newInstance(event.getPlayer());
        var scoreboard = new AnimatedScoreboard(this.plugin, instance, new UnmodifiableAnimationData<>(SCOREBOARD_UPDATE_DELAY, new String[]{BombermanMessages.SCOREBOARD_TITLE.value()}), new AnimationData<>() {
            @Override
            public int getDelay() {
                return SCOREBOARD_UPDATE_DELAY;
            }

            @Override
            public int getNumFrames() {
                return switch (ScoreboardListener.this.plugin.getHeartbeat().getState()) {
                    case LOBBY -> BombermanMessages.SCOREBOARD_WAITING.translate(null, event.getPlayer()).size();
                    case STARTING -> BombermanMessages.SCOREBOARD_STARTING.translate(null, event.getPlayer()).size();
                    case RUNNING -> BombermanMessages.SCOREBOARD_GAME.translate(null, event.getPlayer()).size();
                    case FINISHED -> BombermanMessages.SCOREBOARD_END.translate(null, event.getPlayer()).size();
                };
            }

            @Override
            public String[] getFrame(int index) {
                return switch (ScoreboardListener.this.plugin.getHeartbeat().getState()) {
                    case LOBBY -> BombermanMessages.SCOREBOARD_WAITING.translate(null, event.getPlayer()).get(index).value(event.getPlayer());
                    case STARTING -> BombermanMessages.SCOREBOARD_STARTING.translate(null, event.getPlayer()).get(index).value(event.getPlayer());
                    case RUNNING -> BombermanMessages.SCOREBOARD_GAME.translate(null, event.getPlayer()).get(index).value(event.getPlayer());
                    case FINISHED -> BombermanMessages.SCOREBOARD_END.translate(null, event.getPlayer()).get(index).value(event.getPlayer());
                };
            }
        });
        this.scoreboards.put(event.getPlayer(), scoreboard);
    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        var scoreboard = this.scoreboards.remove(event.getPlayer());
        if (scoreboard != null)
            scoreboard.destroy();
    }
}
