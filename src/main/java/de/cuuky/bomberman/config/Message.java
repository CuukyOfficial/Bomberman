package de.cuuky.bomberman.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import de.cuuky.bomberman.Bomberman;

public enum Message {

	PLAYER_JOIN("playerJoin", "&e%player% &7hat das Spiel betreten!"),

	PLAYER_QUIT("playerQuit", "&e%player% &7hat das Spiel verlassen"),

	PLAYER_TAB("playerTabDisplay", "&7%player%  &c%kills%"),

	PLAYER_DEATH("playerDeath", "&e%player% &7ist gestorben."),

	PLAYER_DEATH_SUICIDE("playerDeathSuicide", "&e%player% &7hat sich selbst hochgejagt!"),

	PLAYER_DEATH_OWN_TNT("playerDeathOwnMedicine",
			"&e%player% &7wurde mit seinem eigenen TNT von &c%killer% weggesprengt!"),

	PLAYER_DEATH_KILLER("playerDeathKill", "&e%player% &7wurde von &c%killer% &7hochgejagt!"),

	PLAYER_DEATH_KILL_SHOCKWAVE("playerDeathShockwave",
			"&e%player% &7wurde mit der Shockwave von &c%killer% &7getötet!"),

	POWERUP_ALL_GAINED("powerUpAllGained", "&7Alle Spieler haben ein &ePowerup &7bekommen!"),

	POWERUP_ALL_REMOVED("powerUpAllRemoved", "&7Alle Spieler haben ihr &ePower-Up verloren&7!"),

	TNT_SET_DELAY("tntSetDelay", "&7Du darfst nur alle &e%seconds% &7Sekunden TNT platzieren!"),

	CHAT_FORMAT("chatFormat", "&7%player%&8» &f%message%"),

	MOTD("Motd", "&eBomberman &7Server\n&eJetzt mitspielen!"),

	TAB_HEADER("TabHeader", "&7Willkommen zu &eBomberman&7!"),

	TAB_FOOTER("TabFooter", "&5Viel Erfolg!");

	private static File file;
	private static YamlConfiguration cfg;

	private String message;
	private String name;
	private String mainMessage;

	private Message(String name, String message) {
		this.name = name;
		this.message = message;
		this.mainMessage = message;
	}

	public String getMessage() {
		return getReplaced(this.message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return this.name;
	}

	public String getMainMessage() {
		return this.mainMessage;
	}

	public static String getReplaced(String msg) {
		return msg.replaceAll("%prefix%", Bomberman.getPrefix());
	}

	public static Message getEntry(String entry) {
		for (Message message : Message.values()) {
			if (!message.getName().equalsIgnoreCase(entry))
				continue;

			return message;
		}

		return null;
	}

	public static void loadAll() {
		file = new File("plugins/Bomberman", "messages.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (Exception e) {
		}

		for (Message message : Message.values())
			cfg.addDefault(message.getName(), message.getMainMessage());

		cfg.options().header(
				"Hinweis: Wenn z.B. der Chat oder der Tab nicht verändert werden soll, einfach '' (also leer machen) dort hin schreiben.\n");
		cfg.options().copyDefaults(true);

		try {
			cfg.save(file);
		} catch (IOException e) {
		}

		for (String string : cfg.getKeys(true)) {
			Message msg = Message.getEntry(string);
			if (msg == null)
				return;

			msg.setMessage(cfg.getString(string).replaceAll("&", "§"));
		}
	}
}
