package de.brentspine.ttt.api;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Settings;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KarmaManager {

    private Main plugin;
    private HashMap<String, Integer> karmaAdded;

    public KarmaManager(Main plugin) {
        this.plugin = plugin;
        karmaAdded = new HashMap<>();
    }

    public void onStart() {
        for(Player player : plugin.getPlayers()) {
            karmaAdded.put(player.getName(), Settings.KARMA_ADDED_FOR_PLAYING);
        }
    }

    public void setKarmaAdded(Player player, Integer amount) {
        karmaAdded.put(player.getName(), amount);
    }

    public void addKarma(Player player, Integer amount) {
        player.sendMessage(Main.PREFIX + "§5+" + amount + " Karma");
        try {
            karmaAdded.put(player.getName(), karmaAdded.get(player.getName()) + amount);
        } catch (Exception e) {
            karmaAdded.put(player.getName(), amount + Settings.KARMA_ADDED_FOR_PLAYING);
        }
    }

    public void remove(Player player, Integer amount) {
        player.sendMessage(Main.PREFIX + "§5-" + amount + " Karma");
        try {
            karmaAdded.put(player.getName(), karmaAdded.get(player.getName()) - amount);
        } catch (Exception e) {
            karmaAdded.put(player.getName(), amount + Settings.KARMA_ADDED_FOR_PLAYING);
        }
    }

}
