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

public class LocationMapper implements ObjectMapper<Location> {
    
    public static final LocationMapper INSTANCE = new LocationMapper();
    
    private LocationMapper() {}

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
}
