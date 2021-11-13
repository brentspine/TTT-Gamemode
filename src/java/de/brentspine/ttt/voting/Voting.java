package de.brentspine.ttt.voting;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Dice;

import java.util.ArrayList;
import java.util.Collections;

public class Voting {

    public static final Integer MAP_AMOUNT = 2;

    private Main plugin;
    private ArrayList<Map> maps;
    private Map[] votingMaps;

    public Voting(Main plugin, ArrayList<Map> maps) {
        this.plugin = plugin;
        this.maps = maps;
        votingMaps = new Map[MAP_AMOUNT];

        chooseRandomMaps();
    }

    private void chooseRandomMaps() {
        for (int i = 0; i < votingMaps.length; i++) {
            Collections.shuffle(maps);
            votingMaps[i] = maps.remove(0);
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

}
