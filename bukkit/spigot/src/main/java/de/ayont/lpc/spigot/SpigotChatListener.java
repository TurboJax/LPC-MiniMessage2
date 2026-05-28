package de.ayont.lpc.spigot;

import de.ayont.lpc.api.LPC;
import de.ayont.lpc.api.LPCChatRenderer;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotChatListener implements Listener {
    private final LPCChatRenderer chatRenderer;

    public SpigotChatListener(JavaPlugin plugin) {
        this.chatRenderer = new SpigotChatRenderer(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent event) {
        User user =
                LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(event.getPlayer());
        event.setFormat(
                LPC.getLegacySerializer().serialize(chatRenderer.render(user, event.getMessage())));
    }
}
