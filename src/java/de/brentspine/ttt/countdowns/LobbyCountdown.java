package de.brentspine.ttt.countdowns;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.gamestates.LobbyState;
import org.bukkit.Bukkit;

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
                        Bukkit.broadcastMessage(Main.PREFIX + "§7Game starting in §a" + seconds + " seconds§7.");
                        break;
                    case 1:
                        Bukkit.broadcastMessage(Main.PREFIX + "§7Game starting in §a" + seconds + " second§7.");
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
