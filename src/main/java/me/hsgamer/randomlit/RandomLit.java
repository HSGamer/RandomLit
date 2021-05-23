package me.hsgamer.randomlit;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;
import java.util.stream.Collectors;

public final class RandomLit extends BasePlugin implements Listener {
    private final MainConfig mainConfig = new MainConfig(this);
    private final Set<UUID> uuidList = new HashSet<>();
    private final List<LightRunnable> runnableList = new LinkedList<>();

    @Override
    public void load() {
        mainConfig.setup();
        MessageUtils.setPrefix("&f[&7RandomLit&f] ");
    }

    @Override
    public void enable() {
        registerListener(this);
        registerCommand(new ToggleSetupCommand(this));
    }

    @Override
    public void postEnable() {
        loadLocations();
    }

    @Override
    public void disable() {
        runnableList.clear();
    }

    void toggleUUID(UUID uuid) {
        if (uuidList.contains(uuid)) {
            uuidList.remove(uuid);
        } else {
            uuidList.add(uuid);
        }
    }

    void removeRunnable(LightRunnable runnable) {
        runnableList.remove(runnable);
        List<Location> locations = runnableList.stream().map(LightRunnable::getLocation).collect(Collectors.toList());
        MainConfig.LOCATIONS.setValue(locations);
        MainConfig.LOCATIONS.getConfig().save();
    }

    public boolean isToggled(UUID uuid) {
        return uuidList.contains(uuid);
    }

    private void loadLocations() {
        for (Location location : MainConfig.LOCATIONS.getValue()) {
            runnableList.add(new LightRunnable(this, location));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!uuidList.contains(event.getPlayer().getUniqueId())) {
            return;
        }
        if (!event.hasBlock()) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block.getBlockData() instanceof Lightable) {
            Location location = block.getLocation();
            List<Location> locations = runnableList.stream().map(LightRunnable::getLocation).collect(Collectors.toList());
            if (!locations.contains(location)) {
                runnableList.add(new LightRunnable(this, location));
                locations.add(location);
                MainConfig.LOCATIONS.setValue(locations);
                MainConfig.LOCATIONS.getConfig().save();
                MessageUtils.sendMessage(event.getPlayer(), "&aAdded");
            }
        } else {
            MessageUtils.sendMessage(event.getPlayer(), "&cThat block is not lightable");
        }
        event.setCancelled(true);
    }
}
