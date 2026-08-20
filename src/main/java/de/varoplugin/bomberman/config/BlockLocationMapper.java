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
import java.util.Map;

public class BlockLocationMapper implements ObjectMapper<Location> {

    public static final BlockLocationMapper INSTANCE = new BlockLocationMapper();

    private BlockLocationMapper() {}

    @Override
    public @NonNull Location createInstance(@Unmodifiable @NotNull Map<@NotNull String, @NotNull Object> values) throws InvalidTypeException, ValidationException {
        return new Location(Bukkit.getWorld((String) values.get("world")), (Long) values.get("x"), (Long) values.get("y"), (Long) values.get("z"));
    }

    @Override
    public @NotNull @Unmodifiable Map<@NotNull String, @NotNull Object> readValues(@NonNull Location instance) throws InvalidTypeException {
        return Map.of("world", instance.getWorld().getName(), "x", instance.getBlockX(), "y", instance.getBlockY(), "z", instance.getBlockZ());
    }

    @Override
    public @NotNull Class<Location> getObjectClass() {
        return Location.class;
    }

    @Override
    public @NotNull Property<?> @NotNull [] getProperties() {
        return new Property[] {
                Property.of("world", Type.STRING),
                Property.of("x", Type.LONG),
                Property.of("y", Type.LONG),
                Property.of("z", Type.LONG),
        };
    }
}
