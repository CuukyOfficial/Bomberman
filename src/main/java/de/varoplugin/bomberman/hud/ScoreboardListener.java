package de.varoplugin.bomberman.hud;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.config.BombermanMessages;
import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import de.varoplugin.cfw.player.hud.AnimatedScoreboard;
import de.varoplugin.cfw.player.hud.AnimationData;
import de.varoplugin.cfw.player.hud.ScoreboardInstance;
import de.varoplugin.cfw.player.hud.UnmodifiableAnimationData;
import io.github.almightysatan.slams.standalone.StandaloneMessageArray2d;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardListener extends AbstractStatePlayerJob {
    
    private static final int SCOREBOARD_UPDATE_DELAY = 20;

    private final Map<Player, AnimatedScoreboard> scoreboards = new HashMap<>();
    private final StandaloneMessageArray2d display;

    public ScoreboardListener(Bomberman plugin, StandaloneMessageArray2d display) {
        super(plugin);

        this.display = display;
    }

    @Override
    public void enable(Player player) {
        ScoreboardInstance instance = ScoreboardInstance.newInstance(player);
        var scoreboard = new AnimatedScoreboard(this.plugin, instance, new UnmodifiableAnimationData<>(SCOREBOARD_UPDATE_DELAY, new String[]{BombermanMessages.SCOREBOARD_TITLE.value()}), new AnimationData<>() {
            @Override
            public int getDelay() {
                return SCOREBOARD_UPDATE_DELAY;
            }

            @Override
            public int getNumFrames() {
                return display.translate(null, player).size();
            }

            @Override
            public String[] getFrame(int index) {
                return display.translate(null, player).get(index).value(player);
            }
        });
        this.scoreboards.put(player, scoreboard);
    }

    @Override
    public void disable(Player player) {
        var scoreboard = this.scoreboards.remove(player);
        if (scoreboard != null)
            scoreboard.destroy();
    }
}
