package de.brentspine.ttt.util;

import de.brentspine.ttt.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigLocationUtil {

    private Main plugin;
    private Location location;
    private String root;
    private File file;
    public YamlConfiguration config;

    public ConfigLocationUtil(Main plugin, Location location, String root) {
        this.plugin = plugin;
        this.location = location;
        this.root = root;
        this.file = new File(plugin.getDataFolder().getPath(),"locations.yml");
        this.config = new YamlConfiguration().loadConfiguration(file);
    }

    public ConfigLocationUtil(Main plugin) {
        this.file = new File(plugin.getDataFolder().getPath(),"locations.yml");
        this.config = new YamlConfiguration().loadConfiguration(file);
    }

    public ConfigLocationUtil(Main plugin, String root) {
        this(plugin, null, root);
    }

    public void saveLocation() {
        config.set(root + ".world", location.getWorld().getName());
        config.set(root + ".x", location.getX());
        config.set(root + ".y", location.getY());
        config.set(root + ".z", location.getZ());
        config.set(root + ".yaw", location.getYaw());
        config.set(root + ".pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location loadLocation() {
        if(!config.contains(root)) {
            return null;
        }
        World world = Bukkit.getWorld(config.getString(root + ".world"));
        double x = config.getDouble(root + ".x");
        double y = config.getDouble(root + ".y");
        double z = config.getDouble(root + ".z");
        float yaw = (float) config.getDouble(root + ".yaw");
        float pitch = (float) config.getDouble(root + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }


    public YamlConfiguration getConfig() {
        return config;
    }
}
