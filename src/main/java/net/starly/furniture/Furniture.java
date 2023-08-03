package net.starly.furniture;

import lombok.Getter;
import net.starly.furniture.command.FurnitureCommand;
import net.starly.furniture.message.MessageContent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Furniture extends JavaPlugin {

    @Getter
    private static Furniture instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // LOAD DATA
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());

        // COMMAND
        getCommand("furniture").setExecutor(new FurnitureCommand());

        // LISTENER
        registerListeners(

        );
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
