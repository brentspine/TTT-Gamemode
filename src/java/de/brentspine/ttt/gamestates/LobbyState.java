package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.countdowns.LobbyCountdown;
import org.bukkit.Bukkit;

public class LobbyState extends GameState {

    public static final int MIN_PLAYERS = 1,
                            MAX_PLAYERS = 12;

    private LobbyCountdown countdown;

    public LobbyState(GameStateManager gameStateManager) {
        countdown = new LobbyCountdown(gameStateManager);
    }

    @Override
    public void start() {
        countdown.startIdle();
    }

    @Override
    public void stop() {
        countdown.stop();
    }

    public LobbyCountdown getCountdown() {
        return countdown;
    }
}
