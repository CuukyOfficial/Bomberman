package de.cuuky.bomberman.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

	public static ItemStack getItem(String displayName, ItemStack iss) {
		ItemMeta issM = iss.getItemMeta();
		issM.setDisplayName(displayName);
		iss.setItemMeta(issM);

		return iss;
	}

	public static ItemStack getSkullOfPlayer(String playerName) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullm = (SkullMeta) skull.getItemMeta();
		skullm.setDisplayName("§7" + playerName);
		skullm.setOwner(playerName);
		skull.setItemMeta(skullm);
		return skull;
	}

	public static ItemStack getItem(String displayName, ItemStack iss, List<String> lore) {
		ItemMeta issM = iss.getItemMeta();
		issM.setDisplayName(displayName);
		issM.setLore(lore);
		iss.setItemMeta(issM);

		return iss;
	}

	public static ItemStack getItem(String displayName, ItemStack iss, String[] lore) {
		ItemMeta issM = iss.getItemMeta();
		issM.setDisplayName(displayName);
		List<String> list = new ArrayList<String>();
		for (String s : lore)
			list.add(s);
		issM.setLore(list);
		iss.setItemMeta(issM);

		return iss;
	}
}
