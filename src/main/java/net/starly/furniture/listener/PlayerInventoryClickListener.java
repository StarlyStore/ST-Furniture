package net.starly.furniture.listener;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.core.util.InventoryUtil;
import net.starly.furniture.builder.ItemBuilder;
import net.starly.furniture.manager.FurnitureManager;
import net.starly.furniture.message.MessageContent;
import net.starly.furniture.message.MessageType;
import net.starly.furniture.page.PaginationInventoryHolder;
import net.starly.furniture.page.PaginationManager;
import net.starly.furniture.util.FurnitureNbtUtil;
import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        FurnitureUtil furnitureUtil = FurnitureUtil.getInstance();
        Map<UUID, Entity> playerEntityMap = FurnitureUtil.getPlayerEntityMap();
        Map<UUID, Boolean> playerInventoryMap = FurnitureUtil.getPlayerInventoryMap();
        Map<UUID, ItemStack> playerFurnitureMap = FurnitureUtil.getPlayerFurnitureMap();
        Map<String, Integer> furnitureMap = FurnitureManager.getFurnitureMap();
        MessageContent content = MessageContent.getInstance();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

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
                    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                        if (InventoryUtil.getSpace(player.getInventory()) > 0) {
                            player.getInventory().addItem(playerFurnitureMap.get(player.getUniqueId()));
                        } else {
                            content.getMessageAfterPrefix(MessageType.ERROR, "noSpaceInInventory").ifPresent(player::sendMessage);
                            return;
                        }
                    }
                    furnitureUtil.removeArmorStand((ArmorStand) armorStand);
                    player.closeInventory();

                    String furnitureName = playerFurnitureMap.get(player.getUniqueId()).getItemMeta().getDisplayName();

                    MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "onRemoveFurniture").ifPresent(message -> {
                        String replacedMessage = message.replace("{name}", furnitureName);
                        player.sendMessage(replacedMessage);
                    });

                    break;
            }
            return;
        }

        if (playerInventoryMap.containsKey(player.getUniqueId())) {
            event.setCancelled(true);

            PaginationInventoryHolder paginationHolder = (PaginationInventoryHolder) event.getClickedInventory().getHolder();
            PaginationManager paginationManager = paginationHolder.getPaginationManager();
            FurnitureNbtUtil furnitureNbtUtil = FurnitureNbtUtil.getInstance();

            if (event.getSlot() == paginationHolder.getPrevBtnSlot()) {
                paginationManager.prevPage();
                FurnitureUtil.getInstance().pageInventory(player, paginationHolder);
                return;
            }

            if (event.getSlot() == paginationHolder.getNextBtnSlot()) {
                paginationManager.nextPage();
                FurnitureUtil.getInstance().pageInventory(player, paginationHolder);
                return;
            }

            if (event.getSlot() > 45) return;

            int clickedFurnitureIndex = paginationManager.getCurrentPage() * 45 - 45 + event.getSlot();

            Set<String> furnitureKey = furnitureMap.keySet();
            List<String> furnitureList = new ArrayList<>(furnitureKey);
            Collections.sort(furnitureList);
            if (furnitureList.size() - 1 < clickedFurnitureIndex) return;
            String furniture = furnitureList.get(clickedFurnitureIndex);

            ItemStack itemStack = new ItemBuilder(Material.IRON_BLOCK)
                    .setName(furniture)
                    .build();

            furnitureNbtUtil.setNbt(itemStack, "customModelData", furnitureMap.get(furniture) + "");
            furnitureNbtUtil.setNbt(itemStack, "furnitureName", furniture);

            if (InventoryUtil.getSpace(player.getInventory()) > 0) {
                player.getInventory().addItem(itemStack);
            } else {
                content.getMessageAfterPrefix(MessageType.ERROR, "noSpaceInInventory").ifPresent(player::sendMessage);
            }
            player.closeInventory();
            return;
        }
    }

}
