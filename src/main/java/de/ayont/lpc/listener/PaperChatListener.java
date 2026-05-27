package de.ayont.lpc.listener;

import de.ayont.lpc.LPC;
import de.ayont.lpc.renderer.PaperChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperChatListener implements Listener {

    private final PaperChatRenderer paperChatRenderer;

    public PaperChatListener(LPC plugin) {
        this.paperChatRenderer = new PaperChatRenderer(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(paperChatRenderer);
    }
}
