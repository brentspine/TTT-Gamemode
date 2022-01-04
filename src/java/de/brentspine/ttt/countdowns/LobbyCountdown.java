package de.brentspine.ttt.countdowns;

import com.google.common.collect.Maps;
import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.gamestates.LobbyState;
import de.brentspine.ttt.voting.Map;
import de.brentspine.ttt.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyCountdown extends Countdown {

    private static final int    COUNTDOWN_TIME = 60,
                                IDLE_TIME = 55;

    private GameStateManager gameStateManager;

    private int seconds;
    private boolean isRunning;
    private int idleID;
    private boolean isIdling;

    public LobbyCountdown(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        seconds = COUNTDOWN_TIME;
    }

    @Override
    public void start() {
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 60:
                    case 20:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                        Bukkit.broadcastMessage(Main.PREFIX + "§7Game starting in §a" + seconds + " seconds§7");

                        if(seconds == 3) {
                            Voting voting = gameStateManager.getPlugin().getVoting();
                            Map winnerMap = new Map(gameStateManager.getPlugin(), "null", "null");
                            if(voting != null) {
                                winnerMap = voting.getWinnerMap();
                            }
                            Bukkit.broadcastMessage(Main.PREFIX + "Voting winner §c" + winnerMap.getDisplayName() + "§7 with §c" + winnerMap.getVotes() + "§7 votes");
                        }

                        break;
                    case 1:
                        Bukkit.broadcastMessage(Main.PREFIX + "§7Game starting in §a" + seconds + " second");
                        break;
                    case 0:
                        gameStateManager.setCurrentGameState(GameState.INGAME_STATE);
                        break;
                    default:
                        break;
                }
                seconds--;
            }
        }, 0, 20);
    }

    @Override
    public void stop() {
        if(isRunning) {
            Bukkit.getScheduler().cancelTask(taskID);
            isRunning = false;
            seconds = COUNTDOWN_TIME;
        }
    }

    public void startIdle() {
        isIdling = true;
        idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(Main.PREFIX + "§7Waiting for §6" +
                        (LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getPlayers().size()) +
                        " §7more players to join");
            }
        }, 0, 20 * IDLE_TIME);
    }

    public void stopIdle() {
        if(isIdling) {
            Bukkit.getScheduler().cancelTask(idleID);
        }
    }

    //Sets the seconds to the value given if its lower than the current one
    public boolean setSecondsIfLower(int seconds) {
        if(seconds < this.seconds) {
            this.seconds = seconds;
            return true;
        }
        return false;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
