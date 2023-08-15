package net.starly.furniture;

import lombok.Getter;
import net.starly.furniture.command.FurnitureCommand;
import net.starly.furniture.listener.BlockPlaceListener;
import net.starly.furniture.listener.PlayerInterectorListener;
import net.starly.furniture.listener.PlayerInventoryClickListener;
import net.starly.furniture.listener.PlayerInventoryCloseListener;
import net.starly.furniture.manager.FurnitureManager;
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
        // LOAD DATAf
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());

        FurnitureManager manager = FurnitureManager.getInstance();
        manager.loadAll();

        // COMMAND
        getCommand("furniture").setExecutor(new FurnitureCommand());
        getCommand("furniture").setTabCompleter(new FurnitureCommand());

        // LISTENER
        registerListeners(
                new BlockPlaceListener(),
                new PlayerInterectorListener(),
                new PlayerInventoryClickListener(),
                new PlayerInventoryCloseListener()
        );
    }

    @Override
    public void onDisable() {
        FurnitureManager manager = FurnitureManager.getInstance();
        manager.saveAll();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
