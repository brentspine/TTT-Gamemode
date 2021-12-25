package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.countdowns.RoleCountdown;
import de.brentspine.ttt.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class InGameState extends GameState {

    private Main plugin;
    private Map map;
    private ArrayList<Player> players;
    private RoleCountdown roleCountdown;

    public InGameState(Main plugin) {
        this.plugin = plugin;
        roleCountdown = new RoleCountdown(plugin);
    }

    @Override
    public void start() {
        Collections.shuffle(plugin.getPlayers());
        players = plugin.getPlayers();

        Bukkit.broadcastMessage(Main.PREFIX + "§aStarting Game");
        map = plugin.getVoting().getWinnerMap();
        map.load();
        for (int i = 0; i < players.size(); i++)
            players.get(i).teleport(map.getSpawnLocations()[i]);

        for(Player current : players) {
            current.getInventory().clear();
        }

        roleCountdown.start();

    }

    @Override
    public void stop() {

    }

}
