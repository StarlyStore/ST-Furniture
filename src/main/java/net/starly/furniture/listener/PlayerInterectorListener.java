package net.starly.furniture.listener;

import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInterectorListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;

        event.setCancelled(true);

        Entity armorStand = event.getRightClicked();

        FurnitureUtil.getInstance().openFurnitureEditGui(player, armorStand);
    }
}
