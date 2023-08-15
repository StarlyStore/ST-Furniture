package net.starly.furniture.util;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FurnitureNbtUtil {

    private static FurnitureNbtUtil instance;

    private FurnitureNbtUtil() {}

    public static FurnitureNbtUtil getInstance() {
        if (instance == null) instance = new FurnitureNbtUtil();
        return instance;
    }

    public String getNbt(ItemStack itemStack, String key) {
        NmsItemStackUtil util = NmsItemStackUtil.getInstance();
        ItemStackWrapper nmsItemStack = util.asNMSCopy(itemStack);
        if (nmsItemStack.getTag() == null) return null;
        NBTTagCompoundWrapper nbtTag = nmsItemStack.getTag();

        return nbtTag.getString(key);
    }

    public void setNbt(ItemStack itemStack, String key, String value) {
        ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();
        nbtTagCompoundWrapper.setString(key, value);

        ItemMeta itemMeta = NmsItemStackUtil.getInstance().asBukkitCopy(itemStackWrapper).getItemMeta();
        itemStack.setItemMeta(itemMeta);
    }

}
