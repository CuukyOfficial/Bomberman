package de.cuuky.bomberman.listener;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.cuuky.bomberman.Bomberman;
import de.cuuky.bomberman.base.Game;
import de.cuuky.bomberman.config.ConfigEntry;
import de.cuuky.bomberman.config.Message;
import de.cuuky.bomberman.scoreboard.ScoreboardSender;
import de.cuuky.bomberman.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Bomberman.getLocationManager().getLobby() == null)
			player.sendMessage(Bomberman.getPrefix() + "§7Es wurde noch §ckeine §7Lobby gesetzt!");
		else
			player.teleport(Bomberman.getLocationManager().getLobby());

		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());

		player.getInventory().clear();
		player.setLevel(0);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 1, false, false));
		event.setJoinMessage(null);
		setTab(player);
		switch (Bomberman.getState()) {
		case START:
			giveItems(player);
			EntityDamageByEntityListener.kills.put(player.getUniqueId().toString(), 0);
			if (!Message.PLAYER_TAB.getMessage().isEmpty())
				player.setPlayerListName(Message.PLAYER_TAB.getMessage().replaceAll("%player%", player.getName())
						.replaceAll("%kills%", String.valueOf(0)));
			Bukkit.broadcastMessage(Message.PLAYER_JOIN.getMessage().replaceAll("%player%", player.getName()));
			player.setGameMode(GameMode.ADVENTURE);
			ScoreboardSender.sendLobbyScoreBoard(player, "§cWaiting...");

			if (Bukkit.getOnlinePlayers().size() == ConfigEntry.MIN_PLAYERS.getValueAsInt())
				Bukkit.broadcastMessage(
						Bomberman.getPrefix() + "§e2 §7Spieler sind nun gejoint, das Spiel wird nun gestartet!");
			break;
		case RUNNING:
			player.setGameMode(GameMode.SPECTATOR);
			ScoreboardSender.sendScoreboard(player, Game.runningCountdown);
			break;
		default:
			break;
		}
	}

	public static void giveItems(Player p) {
		if (ConfigEntry.ALLOW_INFINITE_TNT_MODE.getValueAsBoolean())
			p.getInventory().setItem(8,
					ItemBuilder.getItem("§7Vote für den §cInfinite-TnT-Mode§7!", new ItemStack(Material.PAPER)));
		if (p.hasPermission("bm.start"))
			p.getInventory().setItem(4, ItemBuilder.getItem("§bStart", new ItemStack(Material.DIAMOND)));

		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setAuthor("Cuuky");
		meta.setDisplayName("§aHow to play");
		meta.setTitle("§7How to play §cBomberman");
		meta.addPage(
				"§2§nInhaltsverzeichnis:\n\n§71§8. §6Spielprinzip\n§72§8. §5Power-Ups\n§73§8. §7Liste aller §5PU§7's\n§74§8. §7Liste aller §5PU§7's (2)\n§75§8. §cInfinite-TnT-Mode\n§76§8. §aTipps §7&§a Tricks\n§77§8. §aTipps §7&§a Tricks §7(2)",
				"§2§nSpielprinzip:\n§7In §cBomberman §7geht es darum, deine §cGegner §7mit §cTnT §7wegzusprengen!\n§7Der letzte Überlebende §5gewinnt§7, jedoch wenn die Zeit abläuft, gewinnt der mit den meisten Kills! Sonst gibt es ein §cUnentschieden§7.",
				"§5§nPower-Ups§7:\n§7Man bekommt nach §6einer§7, §65§7, und §69 §7Minuten ein Power-Up, welches zufällig ausgewählt wurde!\n§7Du kannst rechts am Rand immer unter '§6Event§7' mitverfolgen, wann Du das nächste Power-Up bekommen wirst! Nach einer Minute werden dir die Power-Ups entzogen.",
				"§7§nListe aller §5§nPower-Ups§7:\n§7- §a\n§nPunch§7:  §7Du kannst andere Spieler §cwegschlagen§7!\n§7- §a\n§nSpeed§7:\n§7Renne schneller als andere Spieler!\n§7- §a\n§nRegeneration§7:\n§7Wie der Name schon sagt, du regenerierst schneller!",
				"§7... \n§c§nInfinite-TnT§7:\n§7Du kannst unendlich §cTnT §7setzen!§7\n- §a\n§nShockwave§7:\n§7Mit Schlagen + Sneaken kannst du eine Schockwelle auslösen, die Spieler schaden zufügt und alles wegschubst!",
				"§c§nInfinite-TnT-Mode§7:\n§7Du kannst unendlich §cTnT §7setzen, jedoch gibt es keine Power-Ups mehr!\n§7Der Modus kann über das §cVoting §7eingeschaltet werden!",
				"§a§nTipps & Tricks§7:\n§7- Mit Sneaken + gegen das TnT laufen, schießt du das TnT noch weiter weg!\n§7- In deiner XP-Leiste siehst du, wann dein TnT explodiert und wann du ein neues platzieren kannst!\n§7- Time das Wegschießen von §cTnT§7!",
				"... §7- Nutze deine Power-Ups zu Deinem Vorteil!\n\n§aViel Erfolg!");

		book.setItemMeta(meta);
		p.getInventory().setItem(0, book);
	}

	public static void setTab(Player p) {
		PacketPlayOutTitle times = new PacketPlayOutTitle(0, Integer.MAX_VALUE, Integer.MAX_VALUE);
		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE,
				ChatSerializer.a("\"" + Message.TAB_HEADER.getMessage() + "\""));
		PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				ChatSerializer.a("\"" + Message.TAB_FOOTER.getMessage() + "\""));

		for (@SuppressWarnings("rawtypes")
		Packet packet : new Packet[] { times, title, subtitle }) {
			try {
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			} catch (NullPointerException e) {
				return;
			}
		}

		PacketPlayOutPlayerListHeaderFooter headerfooter = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field header = headerfooter.getClass().getDeclaredField("a");
			Field footer = headerfooter.getClass().getDeclaredField("b");
			header.setAccessible(true);
			footer.setAccessible(true);
			header.set(headerfooter, ChatSerializer.a("\"" + Message.TAB_HEADER.getMessage() + "\""));
			footer.set(headerfooter, ChatSerializer.a("\"" + Message.TAB_FOOTER.getMessage() + "\""));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(headerfooter);
	}
}
