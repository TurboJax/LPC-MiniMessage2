package de.ayont.lpc.listener;

import de.ayont.lpc.LPC;
import de.ayont.lpc.renderer.SpigotChatRenderer;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotChatListener implements Listener {
    private final LPC plugin;
    private final SpigotChatRenderer chatRenderer;

    public SpigotChatListener(LPC plugin) {
        this.plugin = plugin;
        this.chatRenderer = new SpigotChatRenderer(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        if (event.getPlayer().hasPermission("lpc.chatcolor")) {
            message = message.replaceAll("§", "&");
            for (Map.Entry<String, String> entry : LPC.getLegacyToMiniMessageCodes().entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
        } else {
            for (Map.Entry<String, String> entry : LPC.getLegacyToMiniMessageCodes().entrySet()) {
                message = message.replace(entry.getValue(), entry.getKey());
            }
        }

        if (plugin.getConfig().getBoolean("use-item-placeholder", false)
                && event.getPlayer().hasPermission("lpc.itemplaceholder")) {
            final ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (!item.getType().equals(Material.AIR)) {
                String itemName = item.getType().toString().toLowerCase().replace("_", " ");
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    StringBuilder hoverText = new StringBuilder();

                    if (meta.hasDisplayName()) {
                        try {
                            Component displayName = meta.displayName();
                            if (displayName != null) {
                                itemName = MiniMessage.miniMessage().serialize(displayName);
                            }
                        } catch (NoSuchMethodError e) {
                            String displayName = meta.getDisplayName();
                            itemName =
                                    MiniMessage.miniMessage()
                                            .serialize(
                                                    LPC.getLegacySerializer()
                                                            .deserialize(displayName));
                        }
                    }

                    if (meta.hasLore()) {
                        try {
                            java.util.List<Component> lore = meta.lore();
                            if (lore != null) {
                                for (Component line : lore) {
                                    hoverText
                                            .append("\n")
                                            .append(MiniMessage.miniMessage().serialize(line));
                                }
                            }
                        } catch (NoSuchMethodError e) {
                            java.util.List<String> lore = meta.getLore();
                            if (lore != null) {
                                for (String line : lore) {
                                    hoverText
                                            .append("\n")
                                            .append(
                                                    MiniMessage.miniMessage()
                                                            .serialize(
                                                                    LPC.getLegacySerializer()
                                                                            .deserialize(line)));
                                }
                            }
                        }
                    }

                    itemName =
                            "<hover:show_text:'"
                                    + itemName
                                    + hoverText.toString()
                                    + "'>"
                                    + itemName
                                    + "</hover>";
                }
                message = message.replaceFirst("(?i)\\[item]", itemName);
            }
        }

        event.setFormat(
                LPC.getLegacySerializer()
                        .serialize(chatRenderer.render(event.getPlayer(), message)));
    }
}
