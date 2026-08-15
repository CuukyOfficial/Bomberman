package de.varoplugin.bomberman.config;

import io.github.almightysatan.jaskl.*;
import io.github.almightysatan.jaskl.entries.BooleanConfigEntry;
import io.github.almightysatan.jaskl.entries.IntegerConfigEntry;
import io.github.almightysatan.jaskl.entries.ListConfigEntry;
import io.github.almightysatan.jaskl.yaml.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BombermanConfig {
    
    private static final Location DEFAULT_LOCATION = new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0);
    
    public static final String CONFIG_DIR = "plugins/bomberman/config/";

    private static final Config CONFIG = YamlConfig.of(new File(CONFIG_DIR + "config.yml"));
    public static final IntegerConfigEntry MIN_PAYERS = IntegerConfigEntry.of(CONFIG, "min_players", "Minimum number of players required", 2, Validator.integerGreater(0));
    public static final IntegerConfigEntry MAX_PAYERS = IntegerConfigEntry.of(CONFIG, "max_players", "Maximum number of players required", 2, Validator.integerGreater(0));
    public static final IntegerConfigEntry TNT_DELAY = IntegerConfigEntry.of(CONFIG, "tnt_delay", "The delay with which tnt can be placed in ticks", 80, Validator.INTEGER_NOT_NEGATIVE);
    public static final IntegerConfigEntry LOBBY_DELAY = IntegerConfigEntry.of(CONFIG, "lobby", "The amount of time that should be spent waiting for more players in seconds", 30, Validator.INTEGER_NOT_NEGATIVE);
    public static final IntegerConfigEntry PROTECTION_START = IntegerConfigEntry.of(CONFIG, "protection", "The initial protection time in seconds", 10, Validator.INTEGER_NOT_NEGATIVE);
    public static final BooleanConfigEntry END_SHUTDOWN = BooleanConfigEntry.of(CONFIG, "end_shutdown", "Whether the server should shut down after the game ends", true);
    public static final BooleanConfigEntry ALLOW_INFINITE_TNT = BooleanConfigEntry.of(CONFIG, "allow_infinite_tnt", "Whether players can vote on infinite tnt", true);
    
    public static final ListConfigEntry<BombermanMap> MAPS = ListConfigEntry.of(CONFIG, "maps", "A list of all available maps",
            Collections.singletonList(new BombermanMap("default", Arrays.asList(DEFAULT_LOCATION, DEFAULT_LOCATION), DEFAULT_LOCATION, DEFAULT_LOCATION)),
            Type.custom(BombermanMap.getMapper()), Validator.listNotEmpty());
    
    private BombermanConfig() {}
    
    public static void init() throws IOException {
        CONFIG.load();
        CONFIG.prune();
        CONFIG.write();
    }
}
