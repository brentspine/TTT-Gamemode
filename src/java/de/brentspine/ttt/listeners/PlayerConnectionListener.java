package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.GameStateManager;
import de.brentspine.ttt.gamestates.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerConnectionListener implements Listener {

    private Main plugin;

    public PlayerConnectionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        if(plugin.getPlayers().size() >= LobbyState.MAX_PLAYERS) {
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }
        plugin.getPlayers().add(player);
        event.setJoinMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7joined the Game (" +
                plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + ")");
        if(plugin.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
            Bukkit.broadcastMessage("Das Spiel würde starten");
        }
    }



    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().remove(player);
        event.setQuitMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7left the Game"); //(" + plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + ")
    }

}
