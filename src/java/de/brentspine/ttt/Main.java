package de.brentspine.ttt;

import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.listeners.PlayerConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    private GameStateManager gameStateManager;
    private ArrayList<Player> players;
    private ArrayList<Player> spectators;

    public static Main instance;
    public static final String PREFIX = "§4§lTTT §8» §7";

    @Override
    public void onEnable() {
        instance = this;
        gameStateManager = new GameStateManager(this);
        players = new ArrayList<>();

        gameStateManager.setCurrentGameState(GameState.LOBBY_STATE);

        init(Bukkit.getPluginManager());
        Bukkit.getConsoleSender().sendMessage(PREFIX + "Das Plugin wurde gestartet.");
    }

    private void init(PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerConnectionListener(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "Das Plugin wurde gestoppt.");
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }
}
