package de.mysterioushacks.tombstoneandinventorylock.listener;

import de.mysterioushacks.tombstoneandinventorylock.TombStoneAndInventoryLock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class AntiGriefListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Iterator<Block> blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.getType() == Material.MOSSY_COBBLESTONE_WALL) {
                if (TombStoneAndInventoryLock.getInstance().getTombStoneManager().isTombStone(block.getLocation())) {
                    blockIterator.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        for (Block block : e.getBlocks()) {
            if (block.getType() == Material.MOSSY_COBBLESTONE_WALL) {
                if (TombStoneAndInventoryLock.getInstance().getTombStoneManager().isTombStone(block.getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        for (Block block : e.getBlocks()) {
            if (block.getType() == Material.MOSSY_COBBLESTONE_WALL) {
                if (TombStoneAndInventoryLock.getInstance().getTombStoneManager().isTombStone(block.getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBurn(BlockBurnEvent e) {
        if (TombStoneAndInventoryLock.getInstance().getTombStoneManager().isTombStone(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }
}
