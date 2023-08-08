package net.starly.furniture.listener;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.furniture.builder.ItemBuilder;
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
    public void onPlayerInteract(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null) return;
        if (heldItem.getType() != Material.IRON_BLOCK) return;

        event.setCancelled(true);

        Location location = event.getBlock().getLocation().add(0.5, 0, 0.5);
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setVisible(false);

        NmsItemStackUtil util = NmsItemStackUtil.getInstance();
        ItemStackWrapper nmsItemStack = util.asNMSCopy(heldItem);
        NBTTagCompoundWrapper nbtTag = nmsItemStack.getTag();
        int customModelData = Integer.parseInt(nbtTag.getString("customModelData"));

        ItemStack itemStack = new ItemBuilder(Material.IRON_HELMET)
                .setCustomModelData(customModelData)
                .build();

        armorStand.setHelmet(itemStack);
    }
}
