package org.dw363.hubitem.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dw363.hubitem.config.HubItemConfig;

import java.util.List;

public class ItemFactory {

    private final HubItemConfig cfg;

    public ItemFactory(HubItemConfig cfg) {
        this.cfg = cfg;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(cfg.getItemMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(cfg.getItemName());

            List<String> lore = cfg.getItemDescription();
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }

            item.setItemMeta(meta);
        }
        return item;
    }
}