package de.ayont.lpc;

import de.ayont.lpc.commands.LPCCommand;
import de.ayont.lpc.listener.AsyncChatListener;
import de.ayont.lpc.listener.SpigotChatListener;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public final class LPC extends JavaPlugin {
    private boolean isPaper;

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

    public static LegacyComponentSerializer getLegacySerializer() {
        return legacySerializer;
    }

    public static Map<String,String> getLegacyToMiniMessageCodes() {
        return Map.copyOf(legacyToMiniMessageCodes);
    }

    @Override
    public void onEnable() {
        this.isPaper = checkIfPaper();
        registerCommand();
        saveDefaultConfig();
        registerListeners();
    }

    public void registerCommand() {
        String commandName = "lpc";
        LPCCommand lpcCommand = new LPCCommand(this);

        this.getCommand(commandName).setExecutor(lpcCommand);
        this.getCommand(commandName).setTabCompleter(lpcCommand);
    }

    private boolean checkIfPaper() {
        try {
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            getLogger().info("Paper API has been detected and will be used.");
            return true;
        } catch (ClassNotFoundException e) {
            getLogger().info("Spigot API has been detected and will be used.");
            return false;
        }
    }

    private void registerListeners() {
        if (isPaper) {
            getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
        } else {
            getServer().getPluginManager().registerEvents(new SpigotChatListener(this), this);
        }
    }

    public boolean isPaper() {
        return isPaper;
    }
}
