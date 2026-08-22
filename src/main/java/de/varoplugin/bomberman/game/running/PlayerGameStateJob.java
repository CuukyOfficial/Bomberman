package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.cfw.item.ItemBuilder;
import de.varoplugin.cfw.player.hook.item.PlayerItemHookBuilder;
import de.varoplugin.cfw.player.hud.NameTagGroup;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerGameStateJob extends AbstractStatePlayerJob {

    private final RunningHeartbeat heartbeat;
    private final NameTagGroup nameTagGroup;

    protected PlayerGameStateJob(RunningHeartbeat heartbeat) {
        super(heartbeat.getPlugin());

        this.heartbeat = heartbeat;
        this.nameTagGroup = new NameTagGroup();
    }

    @Override
    public void enable(Player player) {
        BombPlayer bPlayer = this.plugin.getPlayer(player);
        if (this.heartbeat.getCountdown() != 600) {
            bPlayer.enableSpectator(this.plugin);
            player.teleport(this.plugin.getAlive().findAny().map(BombPlayer::getPlayer).orElse(player).getLocation());
            return;
        }

        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 255, false, false, false));

        this.nameTagGroup.register(bPlayer.getScoreboardInstance(), false, "" ,"");

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            new PlayerItemHookBuilder().slot(i)
                    .item(ItemBuilder.itemStack(new ItemStack(Material.TNT)).displayName("§cSprengstoff").build())
                    .movable(false)
                    .droppable(false)
                    .cancel(false).complete(player, this.plugin);
        }
    }

    @Override
    public void disable(Player player) {
        player.getInventory().clear();
        this.nameTagGroup.unRegister(player);
    }
}
