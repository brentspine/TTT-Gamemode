package de.brentspine.ttt.gamestates;

import org.bukkit.Bukkit;

public class InGameState extends GameState {

    @Override
    public void start() {
        Bukkit.broadcastMessage("Starting InGameState");
    }

    @Override
    public void stop() {

    }

}
