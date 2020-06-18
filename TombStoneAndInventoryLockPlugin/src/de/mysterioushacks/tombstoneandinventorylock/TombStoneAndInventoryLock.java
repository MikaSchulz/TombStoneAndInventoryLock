package de.mysterioushacks.tombstoneandinventorylock;

import de.mysterioushacks.tombstoneandinventorylock.tombstone.TombStoneManager;
import de.mysterioushacks.tombstoneandinventorylock.listener.AntiGriefListener;
import de.mysterioushacks.tombstoneandinventorylock.listener.TombStoneListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TombStoneAndInventoryLock extends JavaPlugin {

    private static TombStoneAndInventoryLock instance;
    private TombStoneManager tombStoneManager;

    @Override
    public void onEnable() {

        instance = this;
        tombStoneManager = new TombStoneManager();
        this.getServer().getPluginManager().registerEvents(new TombStoneListener(), instance);
        this.getServer().getPluginManager().registerEvents(new AntiGriefListener(), instance);

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            System.out.println("WARNING: HolographicDisplays is not installed or not enabled.");
            System.out.println("TombStoneAndInventoryLock will be disabled.");
            this.setEnabled(false);
            return;
        }

    }

    @Override
    public void onDisable() {
         getTombStoneManager().removeAll();
    }

    public static TombStoneAndInventoryLock getInstance() {
        return instance;
    }

    public TombStoneManager getTombStoneManager() {
        return tombStoneManager;
    }

}
