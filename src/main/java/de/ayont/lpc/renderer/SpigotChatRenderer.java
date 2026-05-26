package de.ayont.lpc.renderer;

import de.ayont.lpc.LPC;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.track.Track;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class SpigotChatRenderer {
    private final LuckPerms luckPerms;
    private final LPC plugin;
    private final MiniMessage miniMessage;
    private final boolean hasPapi;

    public SpigotChatRenderer(LPC plugin) {
        this.luckPerms = LuckPermsProvider.get();
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        hasPapi = pluginManager.getPlugin("PlaceholderAPI") != null;
    }

    public @NotNull Component render(Player source, String message) {
        final CachedMetaData metaData =
                this.luckPerms.getPlayerAdapter(Player.class).getMetaData(source);
        final String group =
                Objects.requireNonNull(metaData.getPrimaryGroup(), "Primary group cannot be null");

        String plainMessage =
                source.hasPermission("lpc.chatcolor") ? message : miniMessage.stripTags(message);

        // Defaulting to the group format
        String format = plugin.getConfig().getString("group-formats." + group);

        // Searching for an applicable track format
        if (format == null) {            
            ConfigurationSection trackFormatsSection = plugin.getConfig().getConfigurationSection("track-formats");
            if (trackFormatsSection != null) {
                for (String trackName : trackFormatsSection.getKeys(false)) {
                    Track track = this.luckPerms.getTrackManager().getTrack(trackName);
                    if (track == null) continue;
                    if (!track.containsGroup(group)) continue;
                    format = plugin.getConfig().getString("track-formats." + trackName);
                }
            }
        }

        // Falling back to the default format
        if (format == null) {
            format = plugin.getConfig().getString("chat-format");
        }

        format =
                format.replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                        .replace(
                                "{suffix}",
                                metaData.getSuffix() != null ? metaData.getSuffix() : "")
                        .replace("{prefixes}", String.join(" ", metaData.getPrefixes().values()))
                        .replace("{suffixes}", String.join(" ", metaData.getSuffixes().values()))
                        .replace("{world}", source.getWorld().getName())
                        .replace("{name}", source.getName())
                        .replace("{displayname}", source.getDisplayName())
                        .replace(
                                "{username-color}",
                                metaData.getMetaValue("username-color") != null
                                        ? metaData.getMetaValue("username-color")
                                        : "")
                        .replace(
                                "{message-color}",
                                metaData.getMetaValue("message-color") != null
                                        ? metaData.getMetaValue("message-color")
                                        : "")
                        .replace("{message}", plainMessage);

        if (hasPapi) {
            format = PlaceholderAPI.setPlaceholders(source, format);
        }

        return miniMessage.deserialize(format);
    }
}
