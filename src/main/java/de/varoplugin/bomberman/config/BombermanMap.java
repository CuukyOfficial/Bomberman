package de.varoplugin.bomberman.config;

import io.github.almightysatan.jaskl.InvalidTypeException;
import io.github.almightysatan.jaskl.ObjectMapper;
import io.github.almightysatan.jaskl.Type;
import io.github.almightysatan.jaskl.ValidationException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BombermanMap {

    public final String name;
    public final List<Location> spawns;
    public final Location corner0, corner1;

    public BombermanMap(String name, List<Location> spawns, Location corner0, Location corner1) {
        this.name = name;
        this.spawns = spawns;
        this.corner0 = corner0;
        this.corner1 = corner1;
    }

    static ObjectMapper<BombermanMap> getMapper() {
        return new ObjectMapper<>() {
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
                        Property.of("spawns", Type.list(Type.custom(LocationMapper.INSTANCE))),
                        Property.of("corner_0", Type.custom(BlockLocationMapper.INSTANCE)),
                        Property.of("corner_1", Type.custom(BlockLocationMapper.INSTANCE)),
                };
            }
        };
    }
}
