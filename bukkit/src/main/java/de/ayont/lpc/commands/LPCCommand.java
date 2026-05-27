package de.ayont.lpc.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ayont.lpc.LPCPlugin;
import de.ayont.lpc.api.LPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class LPCCommand implements CommandExecutor, TabCompleter {
    private final LPCPlugin plugin;

    public LPCCommand(LPCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] args) {
        if (args.length == 1 && "reload".equals(args[0])) {
            plugin.reloadConfig();
            String rawReloadMessage =
                    plugin.getConfig()
                            .getString(
                                    "reload-message", "<green>Reloaded LPC Configuration!</green>");
            Component message = MiniMessage.miniMessage().deserialize(rawReloadMessage);

            sender.sendMessage(LPC.getLegacySerializer().serialize(message));
            return true;
        }

        return false;
    }

    public List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String alias,
            final String[] args) {
        if (args.length == 1) return Collections.singletonList("reload");

        return new ArrayList<>();
    }
}
