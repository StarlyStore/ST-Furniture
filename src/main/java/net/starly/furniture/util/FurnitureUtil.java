package net.starly.furniture.util;


import lombok.Getter;
import net.starly.furniture.builder.ItemBuilder;
import net.starly.furniture.manager.FurnitureManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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
    private final static Map<UUID, Integer> playerInventoryPageMap = new HashMap<>();

    private FurnitureUtil() {
    }

    public static FurnitureUtil getInstance() {
        if (instance == null) instance = new FurnitureUtil();
        return instance;
    }

    public void createFurniture(String name, int customModelData) {
        FurnitureManager.getFurnitureMap().put(name, customModelData);
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

    public void openFurnitureMenu(Player player, int page) {
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(player, 6 * 9, "가구 메뉴");
        ItemStack itemStack;

        int maxPage = FurnitureManager.getFurnitureMap().size() / 36 + 1;

        try {
            itemStack = new ItemBuilder(Material.valueOf("STAINED_GLASS_PANE"))
                    .setColor(Color.ORANGE)
                    .setName("")
                    .build();
        } catch (Exception ignored) {
            itemStack = new ItemBuilder(Material.valueOf("ORANGE_STAINED_GLASS_PANE"))
                    .setName("")
                    .build();
        }
        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, itemStack);
        }

        itemStack = new ItemBuilder(Material.BOOK)
                .setName("현재 페이지: " + page)
                .build();
        inventory.setItem(49, itemStack);

        itemStack = new ItemBuilder(Material.BARRIER)
                .setName("X")
                .build();
        inventory.setItem(48, itemStack);
        inventory.setItem(50, itemStack);

        if (maxPage - page > 0) {
            itemStack = new ItemBuilder(Material.ARROW)
                    .setName("다음 페이지")
                    .build();
            inventory.setItem(50, itemStack);
        }

        if (page - 1 > 0) {
            itemStack = new ItemBuilder(Material.ARROW)
                    .setName("이전 페이지")
                    .build();
            inventory.setItem(48, itemStack);
        }

        Set<String> furniture = FurnitureManager.getFurnitureMap().keySet();
        ArrayList<String> furnitureList = new ArrayList<>(furniture);

        for (int i = page * 36 - 36; i < page * 36; i++) {
            if (furnitureList.size() > i) {
                itemStack = new ItemBuilder(Material.IRON_BLOCK)
                        .setName(ChatColor.WHITE + furnitureList.get(i))
                        .setLore(ChatColor.WHITE + "커스텀 모델 데이터: " + FurnitureManager.getFurnitureMap().get(furnitureList.get(i)))
                        .setCustomModelData(FurnitureManager.getFurnitureMap().get(furnitureList.get(i)))
                        .build();
                inventory.setItem(i + 9 - ((page - 1) * 36), itemStack);
            }
        }

        playerInventoryPageMap.put(player.getUniqueId(), page);
        player.openInventory(inventory);
    }

    public void rotateLeftArmorStand(ArmorStand armorStand) {
        EulerAngle currentAngle = armorStand.getHeadPose();
        float yaw = armorStand.getLocation().getYaw();
        System.out.println(armorStand.getHeadPose().getY());
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
