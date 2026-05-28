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
        if (checkIfPaper()) {
            Bukkit.getPluginManager().registerEvents(new PaperChatListener(this), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new SpigotChatListener(this), this);
        }
    }

    private static boolean checkIfPaper() {
        try {
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            LPC.LOGGER.info("Paper API has been detected and will be used.");
            return true;
        } catch (ClassNotFoundException e) {
            LPC.LOGGER.info("Spigot API has been detected and will be used.");
            return false;
        }
    }
}
