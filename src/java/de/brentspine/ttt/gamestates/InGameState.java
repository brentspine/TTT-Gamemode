package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;
import org.bukkit.Bukkit;

public class InGameState extends GameState {

    @Override
    public void start() {
        Bukkit.broadcastMessage(Main.PREFIX + "§aStarting Game");
    }

    @Override
    public void stop() {

    }

}
