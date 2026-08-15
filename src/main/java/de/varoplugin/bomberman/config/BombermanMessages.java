package de.varoplugin.bomberman.config;

import io.github.almightysatan.jaskl.Resource;
import io.github.almightysatan.jaskl.yaml.YamlConfig;
import io.github.almightysatan.slams.PlaceholderResolver;
import io.github.almightysatan.slams.bukkit.BukkitMessage;
import io.github.almightysatan.slams.bukkit.BukkitPlaceholders;
import io.github.almightysatan.slams.parser.JasklParser;
import io.github.almightysatan.slams.standalone.StandaloneMessage;
import io.github.almightysatan.slams.standalone.StandaloneMessageArray2d;
import io.github.almightysatan.slams.standalone.StandaloneSlams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class BombermanMessages {

    private static final StandaloneSlams SLAMS = StandaloneSlams.of("de");

    private static final PlaceholderResolver PLACEHOLDERS;

    static {
        PlaceholderResolver.Builder builder = PlaceholderResolver.builder().builtIn();
        BukkitPlaceholders.addBuiltIn(builder);
        builder.variable("num_players", Bukkit.getOnlinePlayers()::size)
                .variable("num_alive", () -> "TODO")
                .variable("winner", () -> "TODO")
                .variable("event", () -> "TODO")
                .variable("power_up", () -> "TODO")
                .variable("min", () -> "TODO")
                .variable("sec", () -> "TODO");
        PLACEHOLDERS = builder.build();
    }

    public static final BukkitMessage LOBBY_WAITING = BukkitMessage.of("lobby.waiting", SLAMS, PLACEHOLDERS);
    public static final BukkitMessage LOBBY_STARTING = BukkitMessage.of("lobby.starting", SLAMS, PLACEHOLDERS);

    public static final StandaloneMessage SCOREBOARD_TITLE = StandaloneMessage.of("scoreboard.title", SLAMS, PLACEHOLDERS);
    public static final StandaloneMessageArray2d SCOREBOARD_WAITING = StandaloneMessageArray2d.of("scoreboard.waiting", SLAMS, PLACEHOLDERS);
    public static final StandaloneMessageArray2d SCOREBOARD_STARTING = StandaloneMessageArray2d.of("scoreboard.starting", SLAMS, PLACEHOLDERS);
    public static final StandaloneMessageArray2d SCOREBOARD_GAME = StandaloneMessageArray2d.of("scoreboard.game", SLAMS, PLACEHOLDERS);
    public static final StandaloneMessageArray2d SCOREBOARD_END = StandaloneMessageArray2d.of("scoreboard.end", SLAMS, PLACEHOLDERS);

    public static void broadcast(BukkitMessage message) {
        for (Player player : Bukkit.getOnlinePlayers())
            message.send(player, player);
    }

    public static void init() throws IOException {
        SLAMS.load("de", JasklParser.createReadParser(YamlConfig.of(Resource.of(BombermanMessages.class.getClassLoader().getResource("de.yml")))),
                JasklParser.createReadWriteParser(YamlConfig.of(new File(BombermanConfig.CONFIG_DIR + "de.yml"))));
    }
}
