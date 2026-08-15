package de.cuuky.bomberman.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockManager {
    public ArrayList<Location> savedBlocks = new ArrayList<>();
    private boolean changed = false;

    public BlockManager() {
        File file = new File("plugins/Bomberman", "map.yml");
        if (!file.exists()) return;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String key : cfg.getKeys(true)) savedBlocks.add((Location) cfg.get(key));
    }

    public void restore() {
        for (Location loc : savedBlocks) loc.getWorld().getBlockAt(loc).setType(Material.CLAY);
    }

    public void load(Location loc1, Location loc2) {
        savedBlocks.clear();
        changed = true;
        for (Block block : getBlocksBetweenPoints(loc1, loc2)) savedBlocks.add(block.getLocation());
    }

    public void save() {
        if (savedBlocks.isEmpty() || !changed) return;
        File file = new File("plugins/Bomberman", "map.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        int i = -1;
        for (Location loc : savedBlocks) {
            i++;
            cfg.set(String.valueOf(i), loc);
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
        }
    }

    public List<Block> getBlocksBetweenPoints(Location l1, Location l2) {
        List<Block> blocks = new ArrayList<Block>();
        int topBlockX = (l1.getBlockX() < l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
        int bottomBlockX = (l1.getBlockX() > l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
        int topBlockY = (l1.getBlockY() < l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
        int bottomBlockY = (l1.getBlockY() > l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
        int topBlockZ = (l1.getBlockZ() < l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
        int bottomBlockZ = (l1.getBlockZ() > l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int y = bottomBlockY; y <= topBlockY; y++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    Block block = l1.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.CLAY) blocks.add(block);
                }
            }
        }
        return blocks;
    }
}