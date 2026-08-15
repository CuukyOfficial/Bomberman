package de.cuuky.bomberman.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum ConfigEntry {
    PREFIX("prefix", "Setzt den Prefix vor den Nachrichten.", "&7[&eBomberman&7] "), MAX_PLAYERS("maxPlayers", "Stellt die maximalen Spieler ein.", 4), MIN_PLAYERS("minPlayers", "Stellt die minimalen Spieler ein.", 2), TNT_PLACE_DELAY("tntPlaceDelay", "Stellt den Delay zwischen den Patzierungen eines TnTs ein.", 4), STARTPROTECTION("startProtection", "Ob die Spieler in den ersten 10 Sekunden Schaden bekommen sollen.\nHinweis: Das ist gut, damit die Spieler sich am Start nicht selbst verletzen.", true), RELOAD_ON_END("reloadOnEnd", "Ob der Server am Ende reloaded werden soll.\nHinweis: Sonst wird er am Ende restartet.", false), ALLOW_INFINITE_TNT_MODE("allowInfiniteTnTMode", "Ob Spieler für den Infinite-TnT-Mode voten dürfen.", true), ALLOW_SPECTATORS("allowSpectators", "Stellt ein, ob Spectator erlaubt sein sollen.", true);
    private static File file;
    private static YamlConfiguration cfg;
    private String name;
    private String description;
    private Object value;
    private Object mainValue;

    private ConfigEntry(String name, String description, Object mainValue) {
        this.name = name;
        this.description = description;
        this.mainValue = mainValue;
        this.value = mainValue;
    }

    public static ConfigEntry getEntry(String entry) {
        for (ConfigEntry cEntry : ConfigEntry.values()) {
            if (!cEntry.getName().equalsIgnoreCase(entry)) continue;
            return cEntry;
        }
        return null;
    }

    public static void loadAll() {
        file = new File("plugins/Bomberman", "config.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (Exception e) {
        }
        String header = "Hier ist die Beschreibung der Config:\n";
        for (ConfigEntry entry : ConfigEntry.values()) {
            cfg.addDefault(entry.getName(), entry.getMainValue());
            header = header + "\n" + entry.getDescription() + "\n" + entry.getName() + ": " + entry.getMainValue() + "\n";
        }
        cfg.options().header(header);
        cfg.options().copyDefaults(true);
        try {
            cfg.save(file);
        } catch (IOException e) {
        }
        for (String string : cfg.getKeys(true)) ConfigEntry.getEntry(string).setValue(cfg.get(string));
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getMainValue() {
        return this.mainValue;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public long getValueAsLong() {
        return (long) value;
    }

    public int getValueAsInt() {
        return (int) value;
    }

    public boolean getValueAsBoolean() {
        return (boolean) value;
    }

    public String getValueAsString() {
        return (String) value;
    }
}