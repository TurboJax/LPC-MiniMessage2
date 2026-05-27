package de.ayont.lpc.listener;

import de.ayont.lpc.LPC;
import de.ayont.lpc.renderer.LPCChatRenderer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotChatListener implements Listener {
    private final LPCChatRenderer chatRenderer;

    public SpigotChatListener(LPC plugin) {
        this.chatRenderer = new LPCChatRenderer(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(
                LPC.getLegacySerializer()
                        .serialize(chatRenderer.render(event.getPlayer(), event.getMessage())));
    }
}
