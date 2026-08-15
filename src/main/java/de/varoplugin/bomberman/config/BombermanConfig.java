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
    
    private static final String CONFIG_DIR = "plugins/bomberman/config/";

    private static final Config CONFIG = YamlConfig.of(new File(CONFIG_DIR + "config.yml"));
    public static final IntegerConfigEntry MIN_PAYERS = IntegerConfigEntry.of(CONFIG, "min_players", "Minimum number of players required", 2); // Validator.integerGreater(1)
    public static final IntegerConfigEntry MAX_PAYERS = IntegerConfigEntry.of(CONFIG, "max_players", "Maximum number of players required", 2); // Validator.integerGreater(1)
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

    public static class BombermanMap {

        public final String name;
        public final List<Location> spawns;
        public final Location corner0, corner1;

        public BombermanMap(String name, List<Location> spawns, Location corner0, Location corner1) {
            this.name = name;
            this.spawns = spawns;
            this.corner0 = corner0;
            this.corner1 = corner1;
        }

        private static ObjectMapper<BombermanMap> getMapper() {
            ObjectMapper<Location> locationMapper = new ObjectMapper<Location>() {
                @Override
                public @NonNull Location createInstance(@Unmodifiable @NotNull Map<@NotNull String, @NotNull Object> values) throws InvalidTypeException, ValidationException {
                    return new Location(Bukkit.getWorld((String) values.get("world")), ((BigDecimal) values.get("x")).doubleValue(),
                            ((BigDecimal) values.get("y")).doubleValue(), ((BigDecimal) values.get("z")).doubleValue(),
                            ((BigDecimal) values.get("yaw")).floatValue(), ((BigDecimal) values.get("pitch")).floatValue());
                }

                @Override
                public @NotNull @Unmodifiable Map<@NotNull String, @NotNull Object> readValues(@NonNull Location instance) throws InvalidTypeException {
                    return Map.of("world", instance.getWorld().getName(), "x", instance.getX(), "y", instance.getY(), "z", instance.getZ(), "yaw", instance.getYaw(), "pitch", instance.getPitch());
                }

                @Override
                public @NotNull Class<Location> getObjectClass() {
                    return Location.class;
                }

                @Override
                public @NotNull Property<?> @NotNull [] getProperties() {
                    return new Property[] {
                            Property.of("world", Type.STRING),
                            Property.of("x", Type.BIG_DECIMAL),
                            Property.of("y", Type.BIG_DECIMAL),
                            Property.of("z", Type.BIG_DECIMAL),
                            Property.of("yaw", Type.BIG_DECIMAL),
                            Property.of("pitch", Type.BIG_DECIMAL)
                    };
                }
            };

            return new ObjectMapper<BombermanMap>() {
                @SuppressWarnings("unchecked")
                @Override
                public @NonNull BombermanMap createInstance(@Unmodifiable @NotNull Map<@NotNull String, @NotNull Object> values) throws InvalidTypeException, ValidationException {
                    return new BombermanMap((String) values.get("name"), (List<Location>) values.get("spawns"), (Location) values.get("corner_0"), (Location) values.get("corner_1"));
                }

                @Override
                public @Unmodifiable @NotNull Map<@NotNull String, @NotNull Object> readValues(@NonNull BombermanMap instance) throws InvalidTypeException {
                    return Map.of("name", instance.name, "spawns", instance.spawns, "corner_0", instance.corner0, "corner_1", instance.corner1);
                }

                @Override
                public @NotNull Class<BombermanMap> getObjectClass() {
                    return BombermanMap.class;
                }

                @Override
                public @NotNull Property<?> @NotNull [] getProperties() {
                    return new Property[] {
                            Property.of("name", Type.STRING),
                            Property.of("spawns", Type.list(Type.custom(locationMapper))),
                            Property.of("corner_0", Type.custom(locationMapper)),
                            Property.of("corner_1", Type.custom(locationMapper)),
                    };
                }
            };
        }
    }
}
