package net.starly.furniture.listener;

import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerInventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        FurnitureUtil.getPlayerEntityMap().remove(player.getUniqueId());
        FurnitureUtil.getPlayerInventoryMap().remove(player.getUniqueId());
    }
}
