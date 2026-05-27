package de.ayont.lpc.listener;

import de.ayont.lpc.LPC;
import de.ayont.lpc.renderer.LPCChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperChatListener implements Listener {
    private final LPCChatRenderer chatRenderer;

    public PaperChatListener(LPC plugin) {
        this.chatRenderer = new LPCChatRenderer(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(
                ((source, sourceDisplayName, message, viewer) ->
                        chatRenderer.render(
                                source,
                                PlainTextComponentSerializer.plainText().serialize(message))));
    }
}
