package de.brentspine.ttt.voting;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.LobbyState;
import de.brentspine.ttt.role.Tester;
import de.brentspine.ttt.util.ConfigLocationUtil;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Map {

    private Main plugin;
    private String name;
    private String displayName;
    private String builder;
    private Location[] spawnLocations = new Location[LobbyState.MAX_PLAYERS];
    private Location spectatorSpawn;
    private Tester tester;

    private int votes;

    private File file;
    private YamlConfiguration config;

    public Map(Main plugin, String name, String builder) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        this.displayName = name;
        this.file = new File(plugin.getDataFolder().getPath(),"maps.yml");
        this.config = new YamlConfiguration().loadConfiguration(file);
        this.tester = new Tester(plugin, this);

        if(exists()) {
            if(builder == null) {
                builder = config.getString("maps." + name + ".builder");
            }
        }

        this.builder = builder;
        votes = 0;
    }

    public Map(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getPath(),"maps.yml");
        this.config = new YamlConfiguration().loadConfiguration(file);
    }

    public boolean exists() {
        return (config.getString("maps." + name + ".builder") != null);
    }

    public boolean playable() {
        try {
            ConfigurationSection configurationSection = config.getConfigurationSection("maps." + name);
            if(!configurationSection.contains("builder")) return false;
            configurationSection = new ConfigLocationUtil(plugin).getConfig().getConfigurationSection("maps." + name);
            if(!configurationSection.contains("spectator")) return false;
            for (int i = 0; i < LobbyState.MAX_PLAYERS; i++) {
                if(configurationSection.contains(Integer.toString(i + 1))) return false;
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    //Editieren bei neuen Locations
    public void load() {
        spectatorSpawn = new ConfigLocationUtil(plugin, "maps." + name + ".spectator").loadLocation();
        for (int i = 0; i < Settings.maxPlayers; i++) {
            spawnLocations[i] = new ConfigLocationUtil(plugin, "maps." + name + ".spawn." + (i + 1)).loadLocation();
        }
        if(tester.exists()) {
            tester.load();
        } else
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cEs wurden keine Tester für die Map " + getName() + " eingerichtet");
    }

    public void create() {
        config.set("maps." + name + ".builder", builder);
        config.set("maps." + name + ".displayname", displayName);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        if(exists()) {
            config.set("maps." + name, null);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSpawnLocation(int spawnNumber, Location location) {
        spawnLocations[spawnNumber - 1] = location;
        new ConfigLocationUtil(plugin, location, "maps." + name + ".spawn." + spawnNumber).saveLocation();
    }

    public void removeSpawnLocation(int spawnNumber) {
        spawnLocations[spawnNumber - 1] = null;
    }

    public void setSpectatorSpawn(Location location) {
        spectatorSpawn = location;
        new ConfigLocationUtil(plugin, location, "maps." + name + ".spectator").saveLocation();
    }

    public void addVote() {
        votes++;
    }

    public void removeVote() {
        votes--;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public String getName() {
        return name;
    }

    public String getBuilder() {
        return builder;
    }

    public Location getSpectatorSpawn() {
        //return new ConfigLocationUtil(plugin, new Location(Bukkit.getWorld(config.getString("maps." + name + ".spectator.world")), config.getDouble("maps." + name + ".spectator.x"), config.getDouble("maps." + name + ".spectator.y"), config.getDouble("maps." + name + ".spectator.z")), "maps." + name + ".spectator").loadLocation();
        return spectatorSpawn;
    }

    public Location[] getSpawnLocations() {
        return spawnLocations;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public int getVotes() {
        return votes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Tester getTester() {
        return tester;
    }

}
