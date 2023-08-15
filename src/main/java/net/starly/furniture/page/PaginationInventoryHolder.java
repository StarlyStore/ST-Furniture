package net.starly.furniture.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.furniture.Furniture;
import net.starly.furniture.builder.ItemBuilder;
import net.starly.furniture.manager.FurnitureManager;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class PaginationInventoryHolder implements InventoryHolder {

    private final PaginationManager paginationManager;
    private int prevBtnSlot;
    private int nextBtnSlot;

    @Override
    public Inventory getInventory() {

        Inventory inventory = Furniture.getInstance().getServer().createInventory(this, 54, "페이지: " + paginationManager.getCurrentPage());
        FurniturePage currentPage = paginationManager.getCurrentPageData();

        for (int i = 0; i < currentPage.getItems().size(); i++)
            inventory.setItem(i, currentPage.getItems().get(i));

        ItemStack nonExistPage = new ItemBuilder(Material.BARRIER)
                .setName("X")
                .build();
        inventory.setItem(prevBtnSlot, nonExistPage);
        inventory.setItem(nextBtnSlot, nonExistPage);

        if (paginationManager.getCurrentPage() > 1) {
            ItemStack prevPageItem = new ItemBuilder(Material.ARROW)
                    .setName("이전 페이지")
                    .build();
            inventory.setItem(prevBtnSlot, prevPageItem);
        }
        if (currentPage.getPageNum() < paginationManager.getPages().size()) {
            ItemStack nextPageItem = new ItemBuilder(Material.ARROW)
                    .setName("다음 페이지")
                    .build();
            inventory.setItem(nextBtnSlot, nextPageItem);
        }
        return inventory;
    }
}
