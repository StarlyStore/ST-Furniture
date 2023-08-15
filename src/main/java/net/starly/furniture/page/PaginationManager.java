package net.starly.furniture.page;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaginationManager {

    @Getter private final List<FurniturePage> pages;
    @Getter private int currentPage;

    public PaginationManager(List<ItemStack> items) {
        this.pages = paginateItems(items);
        this.currentPage = 1;
    }

    public void nextPage() {
        if (currentPage < pages.size()) currentPage++;
    }

    public void prevPage() {
        if (currentPage > 1) currentPage--;
    }

    public FurniturePage getCurrentPageData() {
        return pages.get(currentPage - 1);
    }

    public List<FurniturePage> paginateItems(List<ItemStack> items) {
        List<FurniturePage> pages = new ArrayList<>();
        int itemCount = items.size();
        int pageCount = (int) Math.ceil((double) itemCount / 45);
        for (int i = 0; i < pageCount; i++) {
            int start = i * 45;
            int end = Math.min(start + 45, itemCount);
            List<ItemStack> pageItems = items.subList(start, end);
            pages.add(new FurniturePage(i + 1, pageItems));
        }
        return pages;
    }

}
