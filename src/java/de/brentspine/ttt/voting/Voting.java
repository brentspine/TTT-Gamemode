package de.brentspine.ttt.voting;

import de.brentspine.ttt.Main;

import java.util.ArrayList;

public class Voting {

    private Main plugin;
    private ArrayList<Map> maps;

    public Voting(Main plugin, ArrayList<Map> maps) {
        this.plugin = plugin;
        this.maps = maps;
    }

}
