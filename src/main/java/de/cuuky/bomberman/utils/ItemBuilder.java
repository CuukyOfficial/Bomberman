package de.cuuky.bomberman.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    public static ItemStack getItem(String displayName, ItemStack iss) {
        ItemMeta issM = iss.getItemMeta();
        issM.setDisplayName(displayName);
        iss.setItemMeta(issM);
        return iss;
    }
}
