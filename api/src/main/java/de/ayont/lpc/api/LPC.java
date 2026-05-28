package de.ayont.lpc.api;

import java.util.logging.Logger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LPC {
    public static final Logger LOGGER = Logger.getLogger("LPC-MiniMessage");

    private static final LegacyComponentSerializer legacySerializer =
            LegacyComponentSerializer.builder()
                    .character('§')
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    private LPC() {}

    public static LegacyComponentSerializer getLegacySerializer() {
        return legacySerializer;
    }
}
