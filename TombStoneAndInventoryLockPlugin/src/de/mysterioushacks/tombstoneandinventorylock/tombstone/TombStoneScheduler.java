package de.mysterioushacks.tombstoneandinventorylock.tombstone;

import de.mysterioushacks.tombstoneandinventorylock.TombStoneAndInventoryLock;
import org.bukkit.Effect;
import org.bukkit.Sound;

public class TombStoneScheduler implements Runnable {

    TombStone tombStone;
    int remainingTime;

    public TombStoneScheduler(TombStone tombStone, int remainingTime) {
        this.tombStone = tombStone;
        this.remainingTime = remainingTime;
    }

    public void addTime(int addTime) {
        int newTime = remainingTime += addTime;
        if (newTime < 300) {
            this.remainingTime = newTime;
        } else {
            remainingTime = 300;
        }
    }

    @Override
    public void run() {

        remainingTime--;

        if (remainingTime <= 0) {
            if (tombStone.getInventory().getViewers().size() >= 1) {
                remainingTime = 1;
                return;
            }
            tombStone.getLocation().getWorld().playSound(tombStone.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
            tombStone.getLocation().getWorld().playEffect(tombStone.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            tombStone.delete(true, false);
            TombStoneAndInventoryLock.getInstance().getTombStoneManager().getTombStoneList().remove(tombStone);
        }

    }
}
