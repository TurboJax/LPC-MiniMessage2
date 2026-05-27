package de.ayont.lpc.renderer;

import de.ayont.lpc.LPC;
import io.papermc.paper.chat.ChatRenderer;
import java.util.Map;
import java.util.regex.Pattern;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LPCChatRenderer implements ChatRenderer {
    private final LuckPerms luckPerms;
    private final LPC plugin;
    private final MiniMessage miniMessage;
    private final boolean hasPapi;

    public LPCChatRenderer(LPC plugin) {
        this.luckPerms = LuckPermsProvider.get();
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        hasPapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @Override
    public @NotNull Component render(
            @NotNull Player source,
            @NotNull Component sourceDisplayName,
            @NotNull Component message,
            @NotNull Audience viewer) {
        final CachedMetaData metaData =
                this.luckPerms.getPlayerAdapter(Player.class).getMetaData(source);
        final String group = metaData.getPrimaryGroup();
        assert group != null : "Primary group cannot be null";

        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        // Replacing legacy codes
        for (Map.Entry<String, String> entry : LPC.getLegacyToMiniMessageCodes().entrySet()) {
            plainMessage = plainMessage.replace(entry.getKey(), entry.getValue());
        }

        // Escaping all tags if the player doesn't have the permission to use the formatter
        if (!source.hasPermission("lpc.chatcolor")) {
            plainMessage = miniMessage.escapeTags(plainMessage);
        }

        // Parsing the item placeholder
        if (plugin.getConfig().getBoolean("use-item-placeholder", true)) {
            if (source.hasPermission("lpc.itemplaceholder")) {
                ItemStack item = source.getInventory().getItemInMainHand();
                String hoverTag = miniMessage.serialize(item.effectiveName().hoverEvent(item));

                Pattern pattern = Pattern.compile("\\[item]", Pattern.CASE_INSENSITIVE);
                plainMessage = pattern.matcher(plainMessage).replaceAll(hoverTag);
            }
        }

        // Checking if the player's group has a chat format
        String formatKey = "group-formats." + group;
        String format = plugin.getConfig().getString(formatKey);

        // Checking for an applicable track format
        if (format == null) {
            ConfigurationSection trackFormatsSection =
                    plugin.getConfig().getConfigurationSection("track-formats");
            if (trackFormatsSection != null) {
                for (String trackName : trackFormatsSection.getKeys(false)) {
                    Track track = this.luckPerms.getTrackManager().getTrack(trackName);
                    if (track == null) continue;
                    if (track.containsGroup(group)) {
                        formatKey = "track-formats." + trackName;
                        format = plugin.getConfig().getString(formatKey);
                        break;
                    }
                }
            }
        }

        // Falling back to the default format
        if (format == null) {
            format = plugin.getConfig().getString("chat-format");
        }

        // Applying data to the selected format
        format =
                format.replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                        .replace(
                                "{suffix}",
                                metaData.getSuffix() != null ? metaData.getSuffix() : "")
                        .replace("{prefixes}", String.join(" ", metaData.getPrefixes().values()))
                        .replace("{suffixes}", String.join(" ", metaData.getSuffixes().values()))
                        .replace("{world}", source.getWorld().getName())
                        .replace("{name}", source.getName())
                        .replace(
                                "{displayname}",
                                PlainTextComponentSerializer.plainText()
                                        .serialize(source.displayName()))
                        .replace(
                                "{username-color}",
                                metaData.getMetaValue("username-color") != null
                                        ? metaData.getMetaValue("username-color")
                                        : "")
                        .replace(
                                "{message-color}",
                                metaData.getMetaValue("message-color") != null
                                        ? metaData.getMetaValue("message-color")
                                        : "");

        // Replacing the main message part of the format.
        format = format.replace("{message}", plainMessage);

        // Applying placeholders if PAPI is found
        if (hasPapi) {
            format = PlaceholderAPI.setPlaceholders(source, format);
        }

        // Rendering the deserialized message
        return miniMessage.deserialize(format);
    }
}
