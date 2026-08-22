package de.varoplugin.bomberman.game.running;

import de.varoplugin.bomberman.game.AbstractStatePlayerJob;
import de.varoplugin.bomberman.model.BombPlayer;
import de.varoplugin.cfw.item.ItemBuilder;
import de.varoplugin.cfw.player.hook.item.HookItemInteractEvent;
import de.varoplugin.cfw.player.hook.item.PlayerItemHookBuilder;
import de.varoplugin.cfw.player.hud.NameTagGroup;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerGameStateJob extends AbstractStatePlayerJob {

    private final NameTagGroup nameTagGroup = new NameTagGroup();

    protected PlayerGameStateJob(RunningHeartbeat heartbeat) {
        super(heartbeat.getPlugin());
    }

    @Override
    public void enable(Player player) {
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 255, false, false, false));

        BombPlayer bPlayer = this.plugin.getPlayer(player);
        this.nameTagGroup.register(bPlayer.getScoreboardInstance(), false, "" ,"");

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            new PlayerItemHookBuilder().slot(i)
                    .item(ItemBuilder.itemStack(new ItemStack(Material.TNT)).displayName("§cSprengstoff").build())
                    .subscribe(HookItemInteractEvent.class, _ ->
                            player.sendMessage("Du hast gevotet!")).complete(player, this.plugin);
        }
    }

    @Override
    public void disable(Player player) {
    }
}
