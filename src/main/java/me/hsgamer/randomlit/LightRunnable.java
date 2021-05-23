package me.hsgamer.randomlit;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.scheduler.BukkitRunnable;

public class LightRunnable extends BukkitRunnable {
    private final Location location;
    private final RandomLit instance;

    public LightRunnable(RandomLit instance, Location location) {
        this.location = location;
        this.instance = instance;
        long update = MainConfig.UPDATE_TICK.getValue();
        runTaskTimer(instance, update, update);
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void run() {
        Block block = location.getBlock();
        BlockData blockData = block.getBlockData();
        if (!(blockData instanceof Lightable)) {
            instance.removeRunnable(this);
            cancel();
            return;
        }
        Lightable lightable = (Lightable) blockData;
        if (Math.random() <= MainConfig.RANDOM_RATE.getValue()) {
            lightable.setLit(!lightable.isLit());
        }
        block.setBlockData(blockData);
    }
}
