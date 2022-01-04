package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.countdowns.RoleCountdown;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import de.brentspine.ttt.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Collections;

public class InGameState extends GameState implements Listener {

    private Main plugin;
    private Map map;
    private ArrayList<Player> players;
    private ArrayList<Player> spectators;
    private RoleCountdown roleCountdown;
    public boolean grace;
    private boolean running = false;

    private Role winnerRole;

    public InGameState(Main plugin) {
        this.plugin = plugin;
        this.grace = true;
        roleCountdown = new RoleCountdown(plugin);
    }

    @Override
    public void start() {
        running = true;
        Collections.shuffle(plugin.getPlayers());
        players = plugin.getPlayers();
        spectators = new ArrayList<>();

        Bukkit.broadcastMessage(Main.PREFIX + "§aStarting Game");
        map = plugin.getVoting().getFinalVotingWinner();
        map.load();
        for (int i = 0; i < players.size(); i++)
            players.get(i).teleport(map.getSpawnLocations()[i]);

        for(Player current : players) {
            current.setHealth(20);
            current.setFoodLevel(20);
            current.setGameMode(GameMode.SURVIVAL);
            current.getInventory().clear();
            plugin.getRoleInventories().getPointManager().setPlayerPoints(current, Settings.STARTING_CAPITAL_POINTS);
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

    public void addSpectator(Player player) {
        spectators.add(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(map.getSpectatorSpawn());

        player.setAllowFlight(true);
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.hidePlayer(plugin, player);
        }
        player.getInventory().setChestplate(null);
        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setDisplayName("§aTeleporter").setLore("§7Teleportiere dich zu einem Spieler §8(Rechtsklick)").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.PAPER).setDisplayName("§9Nächste Runde").setLore("§7Betrete die nächste Runde §8(Rechtsklick)").build());
        players.remove(player);
    }

    @Override
    public void stop() {
        Bukkit.broadcastMessage(Main.PREFIX + "Das Spiel ist zu Ende");
        Bukkit.broadcastMessage(Main.PREFIX + "Die " + winnerRole.getChatColor() + winnerRole.getName() + "§7 haben gewonnen");
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public void setGrace(boolean grace) {
        this.grace = grace;
    }

    public boolean isGrace() {
        return grace;
    }

    public RoleCountdown getRoleCountdown() {
        return roleCountdown;
    }

    public Map getMap() {
        return map;
    }
}
