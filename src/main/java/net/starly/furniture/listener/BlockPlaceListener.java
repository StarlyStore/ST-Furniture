package net.starly.furniture.listener;

import net.starly.furniture.builder.ItemBuilder;
import net.starly.furniture.message.MessageContent;
import net.starly.furniture.message.MessageType;
import net.starly.furniture.util.FurnitureNbtUtil;
import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null) return;
        if (heldItem.getType() != Material.IRON_BLOCK) return;
        FurnitureNbtUtil furnitureNbtUtil = FurnitureNbtUtil.getInstance();
        if (null == furnitureNbtUtil.getNbt(heldItem, "furnitureName")) return;

        event.setCancelled(true);

        Location location = event.getBlock().getLocation().add(0.5, 0, 0.5);
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setVisible(false);

        int customModelData = Integer.parseInt(furnitureNbtUtil.getNbt(heldItem, "customModelData"));
        String furnitureName = furnitureNbtUtil.getNbt(heldItem, "furnitureName");

        ItemStack itemStack = new ItemBuilder(Material.IRON_HELMET)
                .setCustomModelData(customModelData)
                .build();

        furnitureNbtUtil.setNbt(itemStack, "owner", player.getUniqueId().toString());
        armorStand.setHelmet(itemStack);

        if (!(player.getGameMode() == GameMode.CREATIVE)) {
            player.getInventory().remove(heldItem);
            heldItem.setAmount(heldItem.getAmount() - 1);
            player.getInventory().addItem(heldItem);
        }

        itemStack = new ItemBuilder(Material.IRON_BLOCK)
                .setName(furnitureName)
                .build();

        furnitureNbtUtil.setNbt(itemStack, "customModelData", String.valueOf(customModelData));
        furnitureNbtUtil.setNbt(itemStack, "furnitureName", furnitureName);

        FurnitureUtil.getPlayerFurnitureMap().put(player.getUniqueId(), itemStack);
        MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "onPlaceFurniture").ifPresent(message -> {
            String replacedMessage = message.replace("{name}", furnitureName);
            player.sendMessage(replacedMessage);
        });
    }
}
