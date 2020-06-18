package de.mysterioushacks.tombstoneandinventorylock.listener;

import de.mysterioushacks.tombstoneandinventorylock.TombStoneAndInventoryLock;
import de.mysterioushacks.tombstoneandinventorylock.tombstone.TombStone;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TombStoneListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        Block block = p.getLocation().getBlock();

        boolean empty = true;
        Inventory inventory = Bukkit.createInventory(null, 36, "§8§lGrabstein");
        for (int i = 9; i < p.getInventory().getSize(); i++) {
            ItemStack itemStack = p.getInventory().getItem(i);
            if (itemStack != null) {
                if (!(itemStack.getType() == Material.RED_STAINED_GLASS_PANE
                        && itemStack.getItemMeta().getDisplayName().startsWith("§c§lKaufe dir Reihe §e§l"))) {
                    inventory.addItem(itemStack);
                    empty = false;
                }
            }
        }
        if (empty) {
            inventory = null;
            return;
        }
        TombStone tombStone = new TombStone(p, inventory);
        TombStoneAndInventoryLock.getInstance().getTombStoneManager().getTombStoneList().add(tombStone);
        block.setType(Material.MOSSY_COBBLESTONE_WALL);

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        setForbiddenItems(e.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        if (e.getPlayer().getWorld().isGameRule("keepInventory")) {

            Player p = e.getPlayer();

            p.setExp(0);
            p.setLevel(0);

            for (int i = 9; i < p.getInventory().getSize(); i++) {
                p.getInventory().setItem(i, new ItemStack(Material.AIR));
            }

            setForbiddenItems(p);

        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getClickedBlock().getType() == Material.MOSSY_COBBLESTONE_WALL) {

                Player p = e.getPlayer();

                if (p.isSneaking()) {
                    return;
                }
                TombStone tombStone = TombStoneAndInventoryLock.getInstance().getTombStoneManager().isPlayersTombStone(p, e.getClickedBlock().getLocation());
                if (tombStone != null) {

                    p.openInventory(tombStone.getInventory());
                    e.setCancelled(true);

                }

            }

        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {

        if (e.getView().getTitle().equals("§8§lGrabstein")) {

            Inventory inventory = e.getInventory();
            TombStone tombStone = TombStoneAndInventoryLock.getInstance().getTombStoneManager().getTombStoneByInventory(inventory);
            boolean empty = true;
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null) {
                    empty = false;
                }
            }

            if (empty) {
                Player p = (Player) e.getPlayer();
                tombStone.getLocation().getWorld().playSound(tombStone.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                tombStone.getLocation().getWorld().playEffect(tombStone.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                tombStone.delete(true, true);
                TombStoneAndInventoryLock.getInstance().getTombStoneManager().getTombStoneList().remove(tombStone);
            } else {
                tombStone.setInventory(inventory);
                tombStone.getTombStoneScheduler().addTime(60);
            }
        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.MOSSY_COBBLESTONE_WALL) {
            TombStone tombStone = TombStoneAndInventoryLock.getInstance().getTombStoneManager().getTombStone(e.getBlock().getLocation());
            if (tombStone != null) {
                Player p = e.getPlayer();
                if (tombStone.getUniqueId() == p.getUniqueId()) {
                    tombStone.getLocation().getWorld().playSound(tombStone.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    tombStone.getLocation().getWorld().playEffect(tombStone.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                    tombStone.delete(false, true);
                    TombStoneAndInventoryLock.getInstance().getTombStoneManager().getTombStoneList().remove(tombStone);
                    e.setDropItems(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER) {
            ItemStack itemStack = e.getCurrentItem();
            if (itemStack.getType() == Material.RED_STAINED_GLASS_PANE && itemStack.getItemMeta().getDisplayName().startsWith("§c§lKaufe dir Reihe §e§l")) {
                e.setCancelled(true);
            }
        }
    }

    private void setForbiddenItems(Player p) {

        if (!p.hasPermission("inventorylock.bar.bottom")) {
            for (int i = 27; i <= 35; i++) {
                p.getInventory().setItem(i, getForbidden(1));
            }
        }
        if (!p.hasPermission("inventorylock.bar.center")) {
            for (int i = 18; i <= 26; i++) {
                p.getInventory().setItem(i, getForbidden(2));
            }
        }
        if (!p.hasPermission("inventorylock.bar.top")) {
            for (int i = 9; i <= 17; i++) {
                p.getInventory().setItem(i, getForbidden(3));
            }
        }
        p.updateInventory();
    }

    private ItemStack getForbidden(int row) {

        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lKaufe dir Reihe §e§l" + row);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    ;
}
