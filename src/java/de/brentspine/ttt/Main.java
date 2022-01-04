package de.brentspine.ttt;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.brentspine.ttt.api.KarmaManager;
import de.brentspine.ttt.commands.*;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.listeners.*;
import de.brentspine.ttt.role.RoleInventories;
import de.brentspine.ttt.role.RoleManager;
import de.brentspine.ttt.util.Dice;
import de.brentspine.ttt.voting.Map;
import de.brentspine.ttt.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collector;

public class Main extends JavaPlugin {

    private GameStateManager gameStateManager;
    private ArrayList<Player> players;
    private Voting voting;
    private ArrayList<Map> maps;
    private RoleManager roleManager;
    private ProtocolManager protocolManager;
    private RoleInventories roleInventories;
    private KarmaManager karmaManager;

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
        roleInventories = new RoleInventories(this);
        karmaManager = new KarmaManager(this);

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

        getCommand("debugCurrentMap").setExecutor(new DebugCurrentMapCommand(this));
        getCommand("debugAllRoles").setExecutor(new DebugAllRolesCommand(this));
        getCommand("devtest").setExecutor(new DevTestCommand());

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
        pluginManager.registerEvents(new VotingListener(this), this);
        pluginManager.registerEvents(new GameProgressListener(this), this);
        pluginManager.registerEvents(new PlayerInGameConnectionListener(this), this);
        pluginManager.registerEvents(new BlockedListeners(this), this);
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new ChestListener(this), this);
        pluginManager.registerEvents(new TesterListener(this), this);
        pluginManager.registerEvents(roleInventories, this);
        pluginManager.registerEvents(new ShopItemListener(this), this);
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

    public Voting getVoting() {
        return voting;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public RoleInventories getRoleInventories() {
        return roleInventories;
    }

    public KarmaManager getKarmaManager() {
        return karmaManager;
    }

}
