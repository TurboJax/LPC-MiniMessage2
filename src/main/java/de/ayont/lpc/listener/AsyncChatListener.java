package de.ayont.lpc.listener;

import static java.util.regex.Pattern.*;

import de.ayont.lpc.LPC;
import de.ayont.lpc.renderer.LPCChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {

    private final LPCChatRenderer lpcChatRenderer;

    public AsyncChatListener(LPC plugin) {
        this.lpcChatRenderer = new LPCChatRenderer(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(lpcChatRenderer);
    }
}
