package de.brentspine.ttt.voting;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Dice;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Voting {

    public static final Integer MAP_AMOUNT = 2;

    private Main plugin;
    private ArrayList<Map> maps;
    private Map[] votingMaps;
    private int[] votingInventoryOrder = new int[]{3, 5};
    private HashMap<String, Integer> playerVotes = new HashMap<>();
    private Inventory votingInventory;

    File file;
    YamlConfiguration config;

    public Voting(Main plugin, ArrayList<Map> maps) {
        this.plugin = plugin;
        this.maps = maps;
        votingMaps = new Map[MAP_AMOUNT];

        this.file = new File(plugin.getDataFolder().getPath(),"maps.yml");
        this.config = new YamlConfiguration().loadConfiguration(file);

        chooseRandomMaps();
        initVotingInventory();
    }

    private void chooseRandomMaps() {
        for (int i = 0; i < votingMaps.length; i++) {
            Collections.shuffle(maps);
            votingMaps[i] = maps.remove(0);
        }
    }

    public void initVotingInventory() {
        if(votingInventory == null) {
            votingInventory = Bukkit.createInventory(null, 1*9, Settings.votingInventoryTitle);
        }
        for (int i = 0; i < votingMaps.length; i++) {
            Map currentMap = votingMaps[i];
            votingInventory.setItem(votingInventoryOrder[i], new ItemBuilder(Material.PAPER)
                    .setDisplayName("§a" + currentMap.getDisplayName() + "§c - §c§l " + currentMap.getVotes())
                    .setLore("§7Built by " + currentMap.getBuilder() + "\n" +
                             "§7Votes: §c" + currentMap.getVotes())  .build());
        }
    }

    public Map getWinnerMap() {
        Map winnerMap = votingMaps[0];
        for (int i = 1; i < votingMaps.length; i++) {
            if(votingMaps[i].getVotes() > winnerMap.getVotes()) {
                winnerMap = votingMaps[i];
            }
            else if(votingMaps[i].getVotes() == winnerMap.getVotes()) {
                if(new Dice(1, 2).roll() == 1) {
                    winnerMap = votingMaps[i];
                }
            }
        }
        return winnerMap;
    }

    public void vote(Player player, int votingMap) {
        if(!Settings.oneTimeVote || !playerVotes.containsKey(player.getName())) {
            votingMaps[votingMap].addVote();
            playerVotes.put(player.getName(), votingMap);
            player.sendMessage(Main.PREFIX + "You voted for §c" + votingMaps[votingMap].getDisplayName() + "§7. The map currently has §c" + votingMaps[votingMap].getVotes() + "§7 votes");
            initVotingInventory();
        } else {
            votingMaps[playerVotes.get(player.getName())].removeVote();
            playerVotes.remove(player.getName());
            vote(player, votingMap);
        }
        //player.sendMessage(Main.PREFIX + "You already voted for §c" + votingMaps[playerVotes.get(player.getName())].getDisplayName());
    }

    public HashMap<String, Integer> getPlayerVotes() {
        return playerVotes;
    }

    public Inventory getVotingInventory() {
        return votingInventory;
    }

    public int[] getVotingInventoryOrder() {
        return votingInventoryOrder;
    }

    public Map[] getVotingMaps() {
        return votingMaps;
    }

}
