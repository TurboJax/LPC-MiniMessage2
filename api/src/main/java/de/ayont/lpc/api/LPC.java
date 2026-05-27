package de.ayont.lpc.api;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LPC {
    private static final Logger logger = Logger.getLogger("LPC-MiniMessage");
    private static final LegacyComponentSerializer legacySerializer =
            LegacyComponentSerializer.builder()
                    .character('§')
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    private static final Map<String, String> legacyToMiniMessageCodes =
            new HashMap<>() {
                {
                    put("&0", "<black>");
                    put("&1", "<dark_blue>");
                    put("&2", "<dark_green>");
                    put("&3", "<dark_aqua>");
                    put("&4", "<dark_red>");
                    put("&5", "<dark_purple>");
                    put("&6", "<gold>");
                    put("&7", "<gray>");
                    put("&8", "<dark_gray>");
                    put("&9", "<blue>");
                    put("&a", "<green>");
                    put("&b", "<aqua>");
                    put("&c", "<red>");
                    put("&d", "<light_purple>");
                    put("&e", "<yellow>");
                    put("&f", "<white>");
                    put("&k", "<obf>");
                    put("&l", "<b>");
                    put("&m", "<st>");
                    put("&n", "<u>");
                    put("&o", "<i>");
                    put("&r", "<reset>");
                }
            };

    private static final boolean isPaper = checkIfPaper();

    private LPC() {}

    public static boolean isPaper() {
        return isPaper;
    }

    public static LegacyComponentSerializer getLegacySerializer() {
        return legacySerializer;
    }

    public static Map<String, String> getLegacyToMiniMessageCodes() {
        return Map.copyOf(legacyToMiniMessageCodes);
    }

    private static boolean checkIfPaper() {
        try {
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            logger.info("Paper API has been detected and will be used.");
            return true;
        } catch (ClassNotFoundException e) {
            logger.info("Spigot API has been detected and will be used.");
            return false;
        }
    }
}