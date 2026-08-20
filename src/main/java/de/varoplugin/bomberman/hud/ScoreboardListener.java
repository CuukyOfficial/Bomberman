package de.varoplugin.bomberman.hud;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.events.BombermanStateSwitchEvent;
import de.varoplugin.bomberman.game.GameState;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import de.varoplugin.cfw.player.hud.AnimationData;
import de.varoplugin.cfw.player.hud.ScoreboardInstance;
import de.varoplugin.cfw.player.hud.UnmodifiableAnimationData;
import io.github.almightysatan.slams.standalone.StandaloneMessageArray2d;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
    
    private static final int SCOREBOARD_UPDATE_DELAY = 20;

    private Bomberman plugin;
    
    public ScoreboardListener(Bomberman plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = this.plugin.getPlayer(event.getPlayer());
        ScoreboardInstance instance = ScoreboardInstance.newInstance(event.getPlayer());
        var scoreboard = new AnimatedScoreboard(this.plugin, instance, new UnmodifiableAnimationData<>(SCOREBOARD_UPDATE_DELAY,
                new String[]{BombermanMessages.SCOREBOARD_TITLE.value()}), this.getAnimationData(player, this.getScoreboardContent(this.plugin.getHeartbeat().getState())));
        player.setScoreboard(scoreboard);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = this.plugin.getPlayer(event.getPlayer());
        player.getScoreboard().destroy();
        player.setScoreboard(null);
    }
    
    @EventHandler
    public void onStateSwitch(BombermanStateSwitchEvent event) {
        var content = this.getScoreboardContent(this.plugin.getHeartbeat().getState());
        this.plugin.getPlayers().forEach(player -> player.getScoreboard().setContent(this.getAnimationData(player, content)));
    }

    private StandaloneMessageArray2d getScoreboardContent(GameState state) {
        return switch (state) {
            case LOBBY -> BombermanMessages.SCOREBOARD_WAITING;
            case STARTING -> BombermanMessages.SCOREBOARD_STARTING;
            case RUNNING -> BombermanMessages.SCOREBOARD_GAME;
            case FINISHED -> BombermanMessages.SCOREBOARD_END;
        };
    }
    
    private AnimationData<String[]> getAnimationData(BombPlayer player, StandaloneMessageArray2d content) {
        return new AnimationData<>() {
            @Override
            public int getDelay() {
                return SCOREBOARD_UPDATE_DELAY;
            }

            @Override
            public int getNumFrames() {
                return content.translate(null, player).size();
            }

            @Override
            public String[] getFrame(int index) {
                return content.translate(null, player).get(index).value(player);
            }
        };
    }
}
