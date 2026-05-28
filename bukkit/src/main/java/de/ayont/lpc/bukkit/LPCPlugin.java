package de.ayont.lpc.bukkit;

import de.ayont.lpc.api.LPC;
import de.ayont.lpc.bukkit.commands.LPCCommand;
import de.ayont.lpc.paper.PaperChatListener;
import de.ayont.lpc.spigot.SpigotChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LPCPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        registerCommand();
        saveDefaultConfig();
        registerListeners();
    }

    public void registerCommand() {
        String commandName = "lpc";
        LPCCommand lpcCommand = new LPCCommand(this);

        this.getCommand(commandName).setExecutor(lpcCommand);
        this.getCommand(commandName).setTabCompleter(lpcCommand);
    }

    public void registerListeners() {
        if (LPC.isPaper()) {
            Bukkit.getPluginManager().registerEvents(new PaperChatListener(this), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new SpigotChatListener(this), this);
        }
    }
}
