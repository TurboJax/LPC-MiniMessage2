package de.ayont.lpc.paper;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperChatListener implements Listener {
    private final PaperChatRenderer chatRenderer;

    public PaperChatListener(JavaPlugin plugin) {
        this.chatRenderer = new PaperChatRenderer(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(chatRenderer);
    }
}
