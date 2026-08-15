package de.cuuky.bomberman.listener;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import de.cuuky.bomberman.tnt.TnT;

public class EntityExplodeListener implements Listener {

	@EventHandler
	public void on(EntityExplodeEvent e) {
		if (!e.getEntity().getType().equals(EntityType.PRIMED_TNT))
			return;

		TnT.getTnT(e.getEntity()).remove();
		Iterator<Block> iter = e.blockList().iterator();
		while (iter.hasNext()) {
			Block b = iter.next();
			if (!b.getType().equals(Material.CLAY))
				iter.remove();
			else {
				b.setType(Material.AIR);
				b.getDrops().clear();
			}
		}

	}
}
