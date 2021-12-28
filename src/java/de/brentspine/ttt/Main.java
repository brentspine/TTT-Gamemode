package de.brentspine.ttt;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.brentspine.ttt.commands.BoomCommand;
import de.brentspine.ttt.commands.SetupCommand;
import de.brentspine.ttt.commands.StartCommand;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.listeners.BlockedListeners;
import de.brentspine.ttt.listeners.GameProgressListener;
import de.brentspine.ttt.listeners.PlayerLobbyConnectionListener;
import de.brentspine.ttt.listeners.VotingListener;
import de.brentspine.ttt.role.RoleManager;
import de.brentspine.ttt.voting.Map;
import de.brentspine.ttt.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    private GameStateManager gameStateManager;
    private ArrayList<Player> players;
    private ArrayList<Player> spectators;
    private Voting voting;
    private ArrayList<Map> maps;
    private RoleManager roleManager;
    private ProtocolManager protocolManager;
    private BlockedListeners blockedListeners;

    public static Main instance;
    public static final String PREFIX = "§4§lTTT §8» §7";
    public static final String NO_PERMISSION = PREFIX + "§cYou don't have enough permissions to do that";

    @Override
    public void onEnable() {
        instance = this;
        gameStateManager = new GameStateManager(this);
        players = new ArrayList<>();
        roleManager = new RoleManager(this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        blockedListeners = new BlockedListeners(this);



        gameStateManager.setCurrentGameState(GameState.LOBBY_STATE);

        init(Bukkit.getPluginManager());
        Bukkit.getConsoleSender().sendMessage(PREFIX + "Das Plugin wurde gestartet.");


        Bukkit.getConsoleSender().sendMessage(" ");


    }

    private void init(PluginManager pluginManager) {
        initVoting();
        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("boom").setExecutor(new BoomCommand());

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
        pluginManager.registerEvents(new VotingListener(this), this);
        pluginManager.registerEvents(new GameProgressListener(this), this);
        pluginManager.registerEvents(blockedListeners, this);
    }

    private void initVoting() {
        maps = new ArrayList<>();
        for(String current : new Map(this).getConfig().getConfigurationSection("maps").getKeys(false)) {
            Map map = new Map(this, current, null);
            if(map.playable()) {
                maps.add(map);
                Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§aMap §e" + map.getName() + "§a wurde geladen");
            }
            else
                Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cMap §4" + map.getName() + "§c is not playable!");
        }
        if(maps.size() >= Voting.MAP_AMOUNT) {
            voting = new Voting(this, maps);
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cThere are not enough maps to create a voting");
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cYou need at least " + Voting.MAP_AMOUNT + " maps");
            voting = null;
        }

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

    public Voting getVoting() {
        return voting;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

}
