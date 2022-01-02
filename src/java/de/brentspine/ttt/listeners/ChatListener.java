package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDefaultChat(AsyncPlayerChatEvent event) {
        if(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState) return;
        event.setFormat(getChatFormat(ChatColor.WHITE, event.getPlayer()) + event.getMessage());
    }

    @EventHandler
    public void onInGameStateChat(AsyncPlayerChatEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) return;

        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        Player player = event.getPlayer();
        if(inGameState.isGrace()) {
            event.setFormat(getChatFormat(ChatColor.WHITE, player) + event.getMessage());
            return;
        }

        if(inGameState.getSpectators().contains(player)) {
            event.setCancelled(true);
            for(Player current : inGameState.getSpectators()) {
                current.sendMessage(getSpectatorChatFormat(player) + event.getMessage());
            }
            return;
        }

        Role playerRole = plugin.getRoleManager().getPlayerRole(player);
        switch (playerRole) {
            case DETECTIVE:
                if(event.getMessage().startsWith("@d")) {
                    event.setCancelled(true);
                    for(String current : plugin.getRoleManager().getDetectivePlayers()) {
                        Bukkit.getPlayer(current).sendMessage(getSpecialChatFormat(playerRole.getChatColor(), player, "@d") + event.getMessage().substring(3));
                    }
                    return;
                }
            case INNOCENT:
                event.setFormat(getChatFormat(playerRole.getChatColor(), player) + event.getMessage());
                break;
            case TRAITOR:
                event.setCancelled(true);
                if(event.getMessage().startsWith("@t")) {
                    for(String current : plugin.getRoleManager().getTraitorPlayers()) {
                        Bukkit.getPlayer(current).sendMessage(getSpecialChatFormat(playerRole.getChatColor(), player, "@t") + event.getMessage().substring(3));
                    }
                    return;
                }
                for(Player current : Bukkit.getOnlinePlayers()) {
                    Role currentRole = plugin.getRoleManager().getPlayerRole(current);
                    switch (currentRole) {
                        case INNOCENT:
                        case DETECTIVE:
                            current.sendMessage(getChatFormat(Role.INNOCENT.getChatColor(), player) + event.getMessage());
                            break;
                        case TRAITOR:
                            current.sendMessage(getChatFormat(Role.TRAITOR.getChatColor(), player) + event.getMessage());
                            break;
                    }
                }
                break;
        }

    }

    private String getChatFormat(ChatColor playerColor, Player player) {
        return "§7" + playerColor + player.getName() + " §8>> §7";
    }

    private String getSpecialChatFormat(ChatColor playerColor, Player player, String prefix) {
        return "§7[" + prefix + "] " + playerColor + player.getName() + " §8>> §7";
    }

    private String getSpectatorChatFormat(Player player) {
        return "§7[Spectator] " + player.getName() + " §8>> §7";
    }



    @EventHandler
    public void ezListener(AsyncPlayerChatEvent event) {
        if(event.getMessage().equalsIgnoreCase("ez"))
            event.setMessage("Ich bin ein kleiner fetter █████████");
    }

}
