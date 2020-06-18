package de.mysterioushacks.tombstoneandinventorylock.tombstone;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TombStoneManager {
    private final List<TombStone> tombStoneList;

    public TombStoneManager() {
        tombStoneList = new ArrayList<>();
    }

    public TombStone isPlayersTombStone(Player p, Location location) {
        for (TombStone tombStone : tombStoneList) {
            if (tombStone.getUniqueId() == p.getUniqueId() && tombStone.getLocation().toString().equals(location.toString())) {
                return tombStone;
            }
        }
        return null;
    }

    public boolean isTombStone(Location location) {
        for (TombStone tombStone : tombStoneList) {
            if (tombStone.getLocation().toString().equals(location.toString())) {
                return true;
            }
        }
        return false;
    }

    public TombStone getTombStoneByInventory(Inventory inventory) {
        for (TombStone tombStone : tombStoneList) {
                if (tombStone.getInventory() == inventory) {
                return tombStone;
            }
        }
        return null;
    }

    public TombStone getTombStone(Location location) {
        for (TombStone tombStone : tombStoneList) {
            if (tombStone.getLocation().toString().equals(location.toString())) {
                return tombStone;
            }
        }
        return null;
    }

    public List<TombStone> getTombStoneList() {
        return tombStoneList;
    }

    public void removeAll() {
        Iterator<TombStone> tombStoneIterator = tombStoneList.iterator();
        while (tombStoneIterator.hasNext()) {
            TombStone tombStone = tombStoneIterator.next();
            tombStone.delete(false, false);
            tombStoneIterator.remove();
        }
    }
}
