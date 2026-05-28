package de.ayont.lpc.bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.ayont.lpc.api.LPCChatRenderer;
import java.util.regex.Pattern;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.ItemSerializer;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitChatRenderer implements LPCChatRenderer {
    private final LuckPerms luckPerms;
    private final JavaPlugin plugin;
    private final MiniMessage miniMessage;
    private final boolean hasPapi;

    public BukkitChatRenderer(JavaPlugin plugin) {
        this.luckPerms = LuckPermsProvider.get();
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        hasPapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @Override
    public @NotNull Component render(User user, String plainMessage) {
        final CachedMetaData metaData = user.getCachedData().getMetaData();

        final Player player = Bukkit.getPlayer(user.getUniqueId());
        assert player != null : "Player needs to have a valid UUID.";

        final String group = metaData.getPrimaryGroup();
        assert group != null : "Primary group cannot be null";

        // Escaping all tags if the player doesn't have the permission to use the formatter
        if (!player.hasPermission("lpc.chatcolor")) {
            plainMessage = miniMessage.escapeTags(plainMessage);
        }

        // Parsing the item placeholder
        if (plugin.getConfig().getBoolean("use-item-placeholder", true)) {
            if (player.hasPermission("lpc.itemplaceholder")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                if (item.getType() != Material.AIR && meta != null) {
                    // Setting up the Item deserializer
                    Gson gson =
                            new GsonBuilder()
                                    .registerTypeAdapter(Item.class, new ItemSerializer())
                                    .create();

                    // Converting the item nbt to a HoverEvent.
                    Item hoverItem = gson.fromJson(meta.getAsString(), Item.class);
                    HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverItem);

                    // Getting the item's name
                    String displayName = "";
                    if (meta.hasDisplayName()) {
                        displayName = meta.getDisplayName();
                    } else {
                        displayName = meta.getItemName();
                    }

                    BaseComponent component = ComponentSerializer.deserialize(displayName);
                    component.setHoverEvent(hoverEvent);

                    var adventure =
                            BungeeComponentSerializer.get()
                                    .deserialize(new BaseComponent[] {component});

                    String hoverTag = miniMessage.serialize(adventure);

                    Pattern pattern = Pattern.compile("\\[item]", Pattern.CASE_INSENSITIVE);
                    plainMessage = pattern.matcher(plainMessage).replaceAll(hoverTag);
                }
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
                        .replace("{world}", player.getWorld().getName())
                        .replace("{name}", player.getName())
                        .replace("{displayname}", ChatColor.stripColor(player.getDisplayName()))
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
            format = PlaceholderAPI.setPlaceholders(player, format);
        }

        // Returning the deserialized message
        return miniMessage.deserialize(format);
    }
}
