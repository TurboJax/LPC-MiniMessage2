package de.ayont.lpc.paper;

import de.ayont.lpc.LPC;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperChatListener implements Listener {
    private final PaperChatRenderer chatRenderer;

    public PaperChatListener(LPCPlugin plugin) {
        this.chatRenderer = new PaperChatRenderer(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(chatRenderer);
    }
}
