package de.brentspine.ttt.role;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Settings;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PointManager {

    private HashMap<String, Integer> points;

    public PointManager() {
        points = new HashMap<>();
    }

    public void setPlayerPoints(Player player, Integer amount) {
        points.put(player.getName(), amount);
    }

    public void addPlayerPoints(Player player, Integer amount) {
        if(amount == 0) return;
        player.sendMessage(Main.PREFIX + "§a+" + amount + " Punkte");
        if(points.containsKey(player.getName())) {
            points.put(player.getName(), points.get(player.getName()) + amount);
            return;
        }
        setPlayerPoints(player, Settings.STARTING_CAPITAL_POINTS + amount);
    }

    public boolean canBuy(Player player, int amount) {
        if(!points.containsKey(player.getName())) {
            setPlayerPoints(player, Settings.STARTING_CAPITAL_POINTS);
            return canBuy(player, amount);
        }
        if(points.get(player.getName()) >= amount) return true;
        return false;
    }

    public void buy(Player player, int amount) {
        points.put(player.getName(), points.get(player.getName()) - amount);
    }

    public void removePoints(Player player, int amount) {
        if(amount == 0) return;
        player.sendMessage(Main.PREFIX + "§4-" + amount + " Punkte");
        points.put(player.getName(), points.get(player.getName()) - amount);
    }

    public Integer getPlayerPoints(Player player) {
        try {
            return points.get(player.getName());
        } catch (Exception e) {
            return Settings.STARTING_CAPITAL_POINTS;
        }
    }

}
