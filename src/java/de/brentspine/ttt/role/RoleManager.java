package de.brentspine.ttt.role;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RoleManager {

    private Main plugin;
    private HashMap<String, Role> playerRoles;
    private ArrayList<Player> players;

    private int traitors, detectives, innocents;

    public RoleManager(Main plugin) {
        this.plugin = plugin;
        playerRoles = new HashMap<>();
        players = plugin.getPlayers();
    }

    public void calculateRoles() {
        int playerAmount = players.size();

        traitors = (int) Math.round(Math.log(playerAmount) * Settings.traitorProbability); //Traitor probability
        detectives = (int) Math.round(Math.log(playerAmount) * Settings.detectiveProbability); //Detectives probability
        innocents = playerAmount - traitors - detectives;

        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Traitor: " + traitors);
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Detectives: " + detectives);
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Innocents: " + innocents);

        Collections.shuffle(plugin.getPlayers());

        int counter = 0;
        for (int i = counter; i < traitors; i++)
            playerRoles.put(players.get(i).getName(), Role.TRAITOR);
        counter += traitors;

        for (int i = counter; i < detectives; i++)
            playerRoles.put(players.get(i).getName(), Role.DETECTIVE);
        counter += detectives;

        for (int i = counter; i < innocents; i++)
            playerRoles.put(players.get(i).getName(), Role.INNOCENT);
    }

    public Role getPlayerRole(Player player) {
        return playerRoles.get(player.getName());
    }

}
