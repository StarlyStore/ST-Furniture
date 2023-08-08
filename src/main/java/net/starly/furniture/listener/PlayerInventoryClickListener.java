package net.starly.furniture.listener;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.core.util.InventoryUtil;
import net.starly.furniture.builder.ItemBuilder;
import net.starly.furniture.manager.FurnitureManager;
import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        FurnitureUtil furnitureUtil = FurnitureUtil.getInstance();
        Map<UUID, Entity> playerEntityMap = FurnitureUtil.getPlayerEntityMap();
        Map<UUID, Integer> playerInventoryPageMap = FurnitureUtil.getPlayerInventoryPageMap();
        Map<String, Integer> furnitureMap = FurnitureManager.getFurnitureMap();

        if (playerEntityMap.containsKey(player.getUniqueId())) {
            event.setCancelled(true);

            Entity armorStand = playerEntityMap.get(player.getUniqueId());
            int clickedSlot = event.getSlot();

            switch (clickedSlot) {
                case 11:
                    furnitureUtil.rotateLeftArmorStand((ArmorStand) armorStand);
                    break;

                case 12:
                    furnitureUtil.rotateRightArmorStand((ArmorStand) armorStand);
                    break;

                case 13:
                    furnitureUtil.rotateUpwardArmorStand((ArmorStand) armorStand);
                    break;

                case 14:
                    furnitureUtil.rotateDownArmorStand((ArmorStand) armorStand);
                    break;

                case 15:
                    furnitureUtil.removeArmorStand((ArmorStand) armorStand);
                    player.closeInventory();
                    break;
            }
        }

        if (playerInventoryPageMap.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            int page = playerInventoryPageMap.get(player.getUniqueId());

            if (event.getSlot() == 48 && event.getClickedInventory().getItem(48).getType().equals(Material.ARROW)) {
                FurnitureUtil.getInstance().openFurnitureMenu(player, page - 1);
                return;
            }
            if (event.getSlot() == 50 && event.getClickedInventory().getItem(50).getType().equals(Material.ARROW)) {
                FurnitureUtil.getInstance().openFurnitureMenu(player, page + 1);
                return;
            }

            if ((event.getSlot() > 44) || (event.getSlot() < 9)) return;

            int clickedFurnitureIndex = page * 36 - 36 + event.getSlot() - 9;

            Set<String> furnitureKey = furnitureMap.keySet();
            ArrayList<String> furnitureList = new ArrayList<>(furnitureKey);
            if (furnitureList.size() - 1 < clickedFurnitureIndex) return;
            String furniture = furnitureList.get(clickedFurnitureIndex);

            ItemStack itemStack = new ItemBuilder(Material.IRON_BLOCK)
                    .setName(furniture)
                    .build();

            ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
            NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();

            nbtTagCompoundWrapper.setString("customModelData", furnitureMap.get(furniture) + "");
            ItemMeta itemMeta = NmsItemStackUtil.getInstance().asBukkitCopy(itemStackWrapper).getItemMeta();

            itemStack.setItemMeta(itemMeta);

            if (InventoryUtil.getSpace(player.getInventory()) > 0) {
                player.getInventory().addItem(itemStack);
            } else {
                player.sendMessage("공간이 없음");
            }
            player.closeInventory();

        }
    }

}
