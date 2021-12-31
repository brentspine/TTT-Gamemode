package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerInGameConnectionListener implements Listener {

    private Main plugin;

    public PlayerInGameConnectionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) {
            return;
        }
        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        Player player = event.getPlayer();
        for(Player current : inGameState.getSpectators()) {
            player.hidePlayer(plugin, current);
        }
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.hidePlayer(plugin, current);
        }
        inGameState.addSpectator(player);
        event.setJoinMessage("");
    }

}
