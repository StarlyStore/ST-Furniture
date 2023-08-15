package net.starly.furniture.listener;

import net.starly.furniture.message.MessageContent;
import net.starly.furniture.message.MessageType;
import net.starly.furniture.util.FurnitureNbtUtil;
import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInterectorListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
        event.setCancelled(true);
        if (!player.isSneaking()) return;

        Entity entity = event.getRightClicked();
        ArmorStand armorStand = (ArmorStand) entity;
        ItemStack helmet = armorStand.getHelmet();
        String owner = FurnitureNbtUtil.getInstance().getNbt(helmet, "owner");

        if (!owner.equalsIgnoreCase(player.getUniqueId().toString()) || !player.isOp()) {
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "noPermission");
            return;
        }

        FurnitureUtil.getInstance().openFurnitureEditGui(player, entity);
    }
}
