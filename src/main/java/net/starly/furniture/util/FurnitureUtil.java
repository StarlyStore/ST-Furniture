package net.starly.furniture.util;


import lombok.Getter;
import net.starly.furniture.builder.ItemBuilder;
import net.starly.furniture.manager.FurnitureManager;
import net.starly.furniture.message.MessageContent;
import net.starly.furniture.message.MessageType;
import net.starly.furniture.page.PaginationInventoryHolder;
import net.starly.furniture.page.PaginationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;


public class FurnitureUtil {

    private static FurnitureUtil instance;

    @Getter
    private final static Map<UUID, Entity> playerEntityMap = new HashMap<>();

    @Getter
    private final static Map<UUID, Boolean> playerInventoryMap = new HashMap<>();

    @Getter
    private final static Map<UUID, ItemStack> playerFurnitureMap = new HashMap<>();

    private FurnitureUtil() {
    }

    public static FurnitureUtil getInstance() {
        if (instance == null) instance = new FurnitureUtil();
        return instance;
    }

    public void createFurniture(String name, int customModelData) {
        FurnitureManager.getFurnitureMap().put(name, customModelData);
    }

    public void removeFurniture(String name) {
        FurnitureManager.getFurnitureMap().remove(name);
    }

    public void openFurnitureEditGui(Player player, Entity armorStand) {
        Inventory inventory = Bukkit.createInventory(player, 3 * 9, "가구 편집");
        playerEntityMap.put(player.getUniqueId(), armorStand);
        ItemStack itemStack;

        itemStack = new ItemBuilder(Material.ITEM_FRAME)
                .setName("왼쪽으로 회전")
                .build();
        inventory.setItem(11, itemStack);

        itemStack = new ItemBuilder(Material.ITEM_FRAME)
                .setName("오른쪽으로 회전")
                .build();
        inventory.setItem(12, itemStack);

        itemStack = new ItemBuilder(Material.ITEM_FRAME)
                .setName("위쪽으로 회전")
                .build();
        inventory.setItem(13, itemStack);

        itemStack = new ItemBuilder(Material.ITEM_FRAME)
                .setName("아래쪽으로 회전")
                .build();
        inventory.setItem(14, itemStack);

        itemStack = new ItemBuilder(Material.BARRIER)
                .setName("삭제")
                .build();
        inventory.setItem(15, itemStack);

        player.openInventory(inventory);
    }

    public void openFurnitureMenu(Player player) {
        if (!FurnitureManager.getInstance().getItems().isEmpty()) {
            player.closeInventory();
            PaginationManager paginationManager = new PaginationManager(FurnitureManager.getInstance().getItems());
            PaginationInventoryHolder paginationHolder = new PaginationInventoryHolder(paginationManager, 48, 50);

            player.openInventory(paginationHolder.getInventory());
            playerInventoryMap.put(player.getUniqueId(), true);
        } else {
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "noItemInMenu").ifPresent(player::sendMessage);
        }
    }

    public void pageInventory(Player player, PaginationInventoryHolder paginationHolder) {
        player.openInventory(paginationHolder.getInventory());
        playerInventoryMap.put(player.getUniqueId(), true);
    }

    public void rotateLeftArmorStand(ArmorStand armorStand) {
        EulerAngle currentAngle = armorStand.getHeadPose();
        float yaw = armorStand.getLocation().getYaw();
        EulerAngle newAngle = currentAngle.add(0, Math.toRadians(yaw + 45), 0);
        armorStand.setHeadPose(newAngle);
    }

    public void rotateRightArmorStand(ArmorStand armorStand) {
        EulerAngle currentAngle = armorStand.getHeadPose();
        float yaw = armorStand.getLocation().getYaw();
        EulerAngle newAngle = currentAngle.add(0, Math.toRadians(yaw - 45), 0);
        armorStand.setHeadPose(newAngle);
    }

    public void rotateUpwardArmorStand(ArmorStand armorStand) {
        EulerAngle currentAngle = armorStand.getHeadPose();
        float pitch = armorStand.getLocation().getPitch();
        EulerAngle newAngle = currentAngle.add(Math.toRadians(pitch - 45), 0, 0);
        armorStand.setHeadPose(newAngle);
    }

    public void rotateDownArmorStand(ArmorStand armorStand) {
        EulerAngle currentAngle = armorStand.getHeadPose();
        float pitch = armorStand.getLocation().getPitch();
        EulerAngle newAngle = currentAngle.add(Math.toRadians(pitch + 45), 0, 0);
        armorStand.setHeadPose(newAngle);
    }

    public void removeArmorStand(ArmorStand armorStand) {
        armorStand.remove();
    }


}
