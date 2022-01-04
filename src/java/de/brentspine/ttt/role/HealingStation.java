package de.brentspine.ttt.role;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealingStation {

    private Main plugin;
    private int taskID, durability;
    private Entity dummy;
    private Location location;

    public HealingStation(Main plugin, Location location) {
        this.plugin = plugin;
        durability = Settings.DETECTIVE_HEALING_STATION_DURABILITY;
        this.location = location;
        generateStation();
    }

    private void generateStation() {
        location.getBlock().setType(Material.BEACON);
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                durability--;
                for(Entity current : location.getWorld().getNearbyEntities(location, Settings.DETECTIVE_HEALING_STATION_RADIUS, Settings.DETECTIVE_HEALING_STATION_RADIUS, Settings.DETECTIVE_HEALING_STATION_RADIUS)) {
                    if(current instanceof Player) {
                        ((Player) current).addPotionEffect(PotionEffectType.REGENERATION.createEffect(40, Settings.DETECTIVE_HEALING_STATION_AMPLIFIER));
                    }
                }
                if(durability <= 0)
                    destroyStation();
            }
        }, 20, 20); //Delay bis zur Ausführung, Delay zwischen Ausführungen
    }

    private void destroyStation() {
        location.getBlock().setType(Material.AIR);
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
