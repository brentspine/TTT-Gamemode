package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.countdowns.RoleCountdown;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class InGameState extends GameState {

    private Main plugin;
    private Map map;
    private ArrayList<Player> players;
    private RoleCountdown roleCountdown;
    public boolean grace;

    private Role winnerRole;

    public InGameState(Main plugin) {
        this.plugin = plugin;
        this.grace = true;
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
            current.setHealth(20);
            current.setFoodLevel(20);
            current.setGameMode(GameMode.SURVIVAL);
            current.getInventory().clear();
        }

        roleCountdown.start();

    }

    public void checkGameEnd() {
        if(plugin.getRoleManager().getTraitorPlayers().size() <= 0) {
            winnerRole = Role.INNOCENT;
            plugin.getGameStateManager().setCurrentGameState(GameState.ENDING_STATE);
        } else if(plugin.getRoleManager().getTraitorPlayers().size() >= plugin.getPlayers().size()) {
            winnerRole = Role.TRAITOR;
            plugin.getGameStateManager().setCurrentGameState(GameState.ENDING_STATE);
        }
    }

    @Override
    public void stop() {
        Bukkit.broadcastMessage(Main.PREFIX + "Das Spiel ist zu Ende");
        Bukkit.broadcastMessage(Main.PREFIX + "Die " + winnerRole.getChatColor() + winnerRole.getName() + "§7 haben gewonnen");
    }

    public void setGrace(boolean grace) {
        this.grace = grace;
    }

    public boolean isGrace() {
        return grace;
    }
}
