package de.brentspine.ttt.countdowns;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RoleCountdown extends Countdown {

    private Main plugin;
    private Integer seconds = Settings.roleCountdownSeconds;

    public RoleCountdown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 30:
                        Bukkit.broadcastMessage(Main.PREFIX + "Noch " + seconds + " Sekunden bis zur Rollenvergabe");
                        break;
                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                        Bukkit.broadcastMessage(Main.PREFIX + "Die Rollen werden in " + seconds + " Sekunden vergeben");
                        break;
                    case 1:
                        Bukkit.broadcastMessage(Main.PREFIX + "Die Rollen werden in einer Sekunde vergeben");
                        break;
                    case 0:
                        stop();
                        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
                        inGameState.setGrace(false);

                        Bukkit.broadcastMessage(Main.PREFIX + "Die Rollen werden vergeben");
                        plugin.getRoleManager().calculateRoles();

                        ArrayList<String> traitorPlayers = plugin.getRoleManager().getTraitorPlayers();
                        ArrayList<String> detectivePlayers = plugin.getRoleManager().getDetectivePlayers();
                        for (Player current : plugin.getPlayers()) {
                            Role role = plugin.getRoleManager().getPlayerRole(current);
                            current.sendTitle(role.getChatColor() + role.getName(), "");
                            current.setDisplayName(role.getChatColor() + current.getName());

                            /*if(role != Role.DETECTIVE) {
                                current.sendMessage(Main.PREFIX + "Die Detectives sind §9" + String.join(", " + detectivePlayers));
                            }
                            if(role == Role.TRAITOR) {
                                current.sendMessage(Main.PREFIX + "Die Traitor sind §c" + String.join(", " + traitorPlayers));
                            }*/
                        }
                        break;
                    default:
                        break;

              }
              seconds--;
            }
        }, 20, 20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
