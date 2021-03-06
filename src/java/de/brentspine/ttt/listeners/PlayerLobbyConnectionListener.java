package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.countdowns.LobbyCountdown;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.gamestates.LobbyState;
import de.brentspine.ttt.util.ConfigLocationUtil;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import de.brentspine.ttt.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class PlayerLobbyConnectionListener implements Listener {

    private Main plugin;
    private ItemStack voteItem;

    public PlayerLobbyConnectionListener(Main plugin) {
        this.plugin = plugin;
        voteItem = new ItemBuilder(Material.NETHER_STAR).setDisplayName(Settings.VOTING_ITEM_NAME).setLore("§7Vote for a map you will be playing on").build();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        Integer playersSize = plugin.getPlayers().size();
        if(playersSize >= LobbyState.MAX_PLAYERS) {
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }
        plugin.getPlayers().add(player);
        playersSize++;
        event.setJoinMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7joined the Game [" +
                playersSize + "/" + LobbyState.MAX_PLAYERS + "]");

        player.getInventory().clear();
        player.getInventory().setChestplate(null);
        player.getInventory().setItem(4, voteItem);
        player.setGameMode(GameMode.SURVIVAL);

        for(Player current : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, current);
            current.showPlayer(plugin, player);
        }

        //System.out.println(player.getName() + ": " + player.getAddress().getHostString() + ":" + player.getAddress().getPort());

        ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "lobby");
        if(locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        } else Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Missing Lobbyspawn Location");

        LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
        LobbyCountdown countdown = lobbyState.getCountdown();
        if(playersSize >= LobbyState.MIN_PLAYERS) {
            if(!countdown.isRunning()) {
                countdown.stopIdle();
                countdown.start();
            }
        }

        if(playersSize >= LobbyState.MAX_PLAYERS) {
            countdown.setSecondsIfLower(10);
        }
        else if(playersSize >= LobbyState.MAX_PLAYERS / 1.5 ) {
            countdown.setSecondsIfLower(20);
        }
        else if(playersSize >= LobbyState.MAX_PLAYERS / 1.8 ) {
            countdown.setSecondsIfLower(30);
        }

    }



    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().remove(player);
        event.setQuitMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7left the Game"); //(" + plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + ")

        LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
        LobbyCountdown countdown = lobbyState.getCountdown();
        if(plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
            if(countdown.isRunning()) {
                countdown.stop();
                countdown.startIdle();
            }
        }

        Voting voting = plugin.getVoting();
        if(voting.getPlayerVotes().containsKey(player.getName())) {
            voting.getVotingMaps()[voting.getPlayerVotes().get(player.getName())].removeVote();
            voting.getPlayerVotes().remove(player.getName());
            voting.initVotingInventory();
        }

    }

}
