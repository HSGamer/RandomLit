package me.hsgamer.randomlit;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.config.AdvancedConfigPath;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.path.DoubleConfigPath;
import me.hsgamer.hscore.config.path.LongConfigPath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainConfig extends PathableConfig {
    public static final LongConfigPath UPDATE_TICK = new LongConfigPath("update-tick", 20L);
    public static final DoubleConfigPath RANDOM_RATE = new DoubleConfigPath("random-rate", 0.5D);
    public static final AdvancedConfigPath<List<String>, List<Location>> LOCATIONS = new AdvancedConfigPath<List<String>, List<Location>>("locations", Collections.emptyList()) {
        @Override
        public List<String> getFromConfig(Config config) {
            Object o = config.get(getPath());
            if (o == null) {
                return Collections.emptyList();
            }
            return CollectionUtils.createStringListFromObject(o, true);
        }

        @Override
        public List<Location> convert(List<String> rawValue) {
            List<Location> locations = new ArrayList<>();
            for (String s : rawValue) {
                String[] split = s.split(",", 4);
                World world = Bukkit.getWorld(split[0]);
                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);
                int z = Integer.parseInt(split[3]);
                locations.add(new Location(world, x, y, z));
            }
            return locations;
        }

        @Override
        public List<String> convertToRaw(List<Location> value) {
            List<String> strings = new ArrayList<>();
            for (Location location : value) {
                strings.add(String.join(",",
                        location.getWorld().getName(),
                        Integer.toString(location.getBlockX()),
                        Integer.toString(location.getBlockY()),
                        Integer.toString(location.getBlockZ())
                ));
            }
            return strings;
        }
    };

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
