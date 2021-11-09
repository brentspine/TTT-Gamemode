package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;

public class LobbyState extends GameState {

    public static final int MIN_PLAYERS = 1,
                            MAX_PLAYERS = 12;

    @Override
    public void start() {
        System.out.println(Main.PREFIX + "§lLobbystate running");
    }

    @Override
    public void stop() {

    }



}
