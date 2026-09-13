package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.Bomberman;
import de.varoplugin.bomberman.game.AbstractStateTimerJob;
import de.varoplugin.bomberman.model.Bomb;
import de.varoplugin.cfw.version.VersionUtils;

import java.util.Comparator;

public class PlayerUiJob extends AbstractStateTimerJob {

    private static final int BARS = 10;

    protected PlayerUiJob(Bomberman plugin) {
        super(plugin, 1, true);
    }

    @Override
    public void run() {
        this.plugin.getAlive().forEach(player -> {
            int time = player.getBombs().map(Bomb::getRemainingSeconds).map(i -> i + 1)
                    .min(Comparator.naturalOrder()).orElse(0);
            player.getPlayer().setLevel(time);

            if (player.getPowerUp() != null) {
                int remainingPowerUpSeconds = Math.toIntExact(player.getPowerUp().calculateRemainingTime());
                int maxDuration = Math.toIntExact(player.getPowerUp().getDuration());
                float overPercent = (float) remainingPowerUpSeconds / maxDuration;

                int cooldownPowerUp = Math.toIntExact(player.getPowerUp().calculateCooldownRemaining());

                StringBuilder actionbar = new StringBuilder();
                // A row of 10 ▒ characters and percentile of them green, others are gray
                for (int i = 0; i < BARS; i++) {
                    if (cooldownPowerUp > 0 && i == BARS / 2) {
                        actionbar.append(" §e").append((cooldownPowerUp / 1000) + 1).append("s ");
                    }

                    if (i <= (int) (overPercent * BARS)) {
                        actionbar.append("§a█");
                    } else {
                        actionbar.append("§7▒");
                    }
                }

                VersionUtils.getVersionAdapter().sendActionbar(player.getPlayer(), actionbar.toString());
            } else {
                VersionUtils.getVersionAdapter().sendActionbar(player.getPlayer(), "§7▒".repeat(BARS));
            }
        });
    }

    @Override
    public void stop() {
        super.stop();

        this.plugin.getPlayers().forEach(player -> {
            player.getPlayer().setLevel(0);
            player.getPlayer().setExp(0);
        });
    }
}
