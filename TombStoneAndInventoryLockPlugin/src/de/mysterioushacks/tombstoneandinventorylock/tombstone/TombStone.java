package de.mysterioushacks.tombstoneandinventorylock.tombstone;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.mysterioushacks.tombstoneandinventorylock.TombStoneAndInventoryLock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TombStone {

    private final UUID uniqueId;
    private final Location location;
    private final Material material;
    private final Hologram hologram;
    private final int taskId;
    private final TombStoneScheduler tombStoneScheduler;
    private Inventory inventory;

    public TombStone(Player p, Inventory inventory) {

        this.uniqueId = p.getUniqueId();
        Location location = p.getPlayer().getLocation().getBlock().getLocation();
        this.location = location;
        this.material = location.getBlock().getType();
        this.inventory = inventory;

        hologram = HologramsAPI.createHologram(TombStoneAndInventoryLock.getInstance(), location.clone().add(0.5, 1.55, 0.5));
        hologram.appendTextLine("§7§lRIP");
        hologram.appendTextLine("§7§l" + p.getName());

        tombStoneScheduler = new TombStoneScheduler(this, 300);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TombStoneAndInventoryLock.getInstance(), tombStoneScheduler, 20, 20);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public TombStoneScheduler getTombStoneScheduler() {
        return tombStoneScheduler;
    }

    public int getTaskId() {
        return taskId;
    }

    public void delete(boolean empty, boolean useSched) {
        if (!empty) {
            for (ItemStack itemStack : getInventory().getContents()) {
                if (itemStack != null) {
                    getLocation().getWorld().dropItemNaturally(getLocation().clone().subtract(0, 0.2, 0), itemStack);
                }
            }
        }
        getHologram().delete();
        if (useSched) {
            Bukkit.getScheduler().runTask(TombStoneAndInventoryLock.getInstance(), () -> getLocation().getBlock().setType(getMaterial()));
        } else {
            getLocation().getBlock().setType(getMaterial());
        }
        Bukkit.getScheduler().cancelTask(getTaskId());

    }
}