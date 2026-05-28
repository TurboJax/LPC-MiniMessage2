package de.ayont.lpc.api;

import java.util.logging.Logger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LPC {
    private static final Logger logger = Logger.getLogger("LPC-MiniMessage");
    private static final LegacyComponentSerializer legacySerializer =
            LegacyComponentSerializer.builder()
                    .character('§')
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    private static final boolean isPaper = checkIfPaper();

    private LPC() {}

    public static boolean isPaper() {
        return isPaper;
    }

    public static LegacyComponentSerializer getLegacySerializer() {
        return legacySerializer;
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
