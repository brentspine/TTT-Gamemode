package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameProgressListener implements Listener {

    private Main plugin;
    private RoleManager roleManager;

    public GameProgressListener(Main plugin) {
        this.plugin = plugin;
        roleManager = plugin.getRoleManager();
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) return;
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        Role damagerRole = roleManager.getPlayerRole(damager);
        Role victimRole = roleManager.getPlayerRole(victim);

        if((damagerRole == Role.INNOCENT || damagerRole == Role.DETECTIVE) && victimRole == Role.DETECTIVE) {
            damager.sendMessage(Main.PREFIX + "§cDu darfst keine Detectives angreifen!");
        }
        if(damagerRole == Role.TRAITOR && victimRole == Role.TRAITOR) {
            event.setDamage(0);
            //damager.sendMessage(Main.PREFIX + "§cDu darfst keine anderen Traitor schlagen!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) return;
        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        Player victim = event.getEntity();
        if(victim.getKiller() == null) return;
        Player killer = victim.getKiller();
        Role victimRole = roleManager.getPlayerRole(victim);
        Role killerRole = roleManager.getPlayerRole(killer);

        switch (killerRole) {
            case TRAITOR:
                if(victimRole == Role.TRAITOR) {
                    killer.sendMessage(Main.PREFIX + "§cDu hast einen anderen Traitor getötet!");
                    killer.kickPlayer(Main.PREFIX + "§cDu hast einen anderen Traitor getötet!");
                    plugin.getPlayers().remove(killer);
                } else {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                }
                break;
            case INNOCENT:
            case DETECTIVE:
                if(victimRole == Role.TRAITOR) {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                } else if(victimRole == Role.INNOCENT) {
                    killer.sendMessage(Main.PREFIX + "§cDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§c ermordet!");
                } else if(victimRole == Role.DETECTIVE) {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                }
                break;
        }

        victim.sendMessage(Main.PREFIX + "§7Du wurdest von " + killerRole.getChatColor() + killer.getName() + "§7 ermordet");
        victim.sendTitle("§cVorbei", "§cDu wurdest ermordet");

        if(victimRole == Role.TRAITOR)
            plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());
        if(victimRole == Role.DETECTIVE)
            plugin.getRoleManager().getDetectivePlayers().remove(victim.getName());
        plugin.getPlayers().remove(victim);

        inGameState.checkGameEnd();
    }


}
