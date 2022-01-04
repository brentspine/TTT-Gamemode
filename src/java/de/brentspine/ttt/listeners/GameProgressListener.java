package de.brentspine.ttt.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.EquivalentConverter;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.*;
import de.brentspine.ttt.Main;
import de.brentspine.ttt.api.KarmaManager;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.PointManager;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.role.RoleManager;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import org.bukkit.ChatColor;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketEncoder;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameProgressListener implements Listener {

    private Main plugin;
    private RoleManager roleManager;
    private PointManager pointManager;
    private KarmaManager karmaManager;

    public GameProgressListener(Main plugin) {
        this.plugin = plugin;
        roleManager = plugin.getRoleManager();
        pointManager = plugin.getRoleInventories().getPointManager();
        karmaManager = plugin.getKarmaManager();
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
        Role victimRole = roleManager.getPlayerRole(victim);

        if(victimRole == Role.TRAITOR)
            plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());
        if(victimRole == Role.DETECTIVE)
            plugin.getRoleManager().getDetectivePlayers().remove(victim.getName());
        plugin.getPlayers().remove(victim);

        event.setDeathMessage("");

        if(victim.getKiller() == null) {
            victim.sendMessage(Main.PREFIX + "§cDu bist gestorben");
            victim.sendTitle("§cVorbei", "§cDu bist gestorben", 5, 65, 10);
            return;
        }
        Player killer = victim.getKiller();
        Role killerRole = roleManager.getPlayerRole(killer);

        switch (killerRole) {
            case TRAITOR:
                if(victimRole == Role.TRAITOR) {
                    killer.sendMessage(Main.PREFIX + "§cDu hast einen anderen Traitor getötet!");
                    pointManager.removePoints(killer, Settings.POINTS_REMOVED_TRAITOR_KILL_TEAMMATE);
                    karmaManager.remove(killer, Settings.KARMA_REMOVED_TRAITOR_KILL_TEAMMATE);
                    if(Settings.KILL_TRAITOR_ON_TEAMMATE_KILL) {
                        killer.setHealth(0);
                        plugin.getPlayers().remove(killer);
                    }
                    //killer.kickPlayer(Main.PREFIX + "§cDu hast einen anderen Traitor getötet!");
                } else {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                    if(victimRole == Role.DETECTIVE) {
                        pointManager.addPlayerPoints(killer, Settings.POINTS_ADDED_TRAITOR_KILL_DETECTIVE);
                        karmaManager.addKarma(killer, Settings.KARMA_ADDED_TRAITOR_KILL_DETECTIVE);
                    }
                    else if(victimRole == Role.INNOCENT) {
                        pointManager.addPlayerPoints(killer, Settings.POINTS_ADDED_TRAITOR_KILL_INNOCENT);
                        karmaManager.addKarma(killer, Settings.KARMA_ADDED_TRAITOR_KILL_INNOCENT);
                    }
                }
                break;
            case DETECTIVE:
                if(victimRole == Role.TRAITOR) {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                    pointManager.addPlayerPoints(killer, Settings.POINTS_ADDED_DETECTIVE_KILL_TRAITOR);
                    karmaManager.addKarma(killer, Settings.KARMA_ADDED_DETECTIVE_KILL_TRAITOR);
                } else if(victimRole == Role.INNOCENT) {
                    killer.sendMessage(Main.PREFIX + "§cDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§c ermordet!");
                    pointManager.removePoints(killer, Settings.POINTS_REMOVED_DETECTIVE_KILL_INNOCENT);
                    karmaManager.remove(killer, Settings.KARMA_REMOVED_DETECTIVE_KILL_INNOCENT);
                } else if(victimRole == Role.DETECTIVE) {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                    pointManager.removePoints(killer, Settings.POINTS_REMOVED_DETECTIVE_KILL_TEAMMATE);
                    karmaManager.remove(killer, Settings.KARMA_REMOVED_DETECTIVE_KILL_TEAMMATE);
                }
                break;
            case INNOCENT:
                if(victimRole == Role.TRAITOR) {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                    karmaManager.addKarma(killer, Settings.KARMA_ADDED_DETECTIVE_KILL_TRAITOR);
                } else if(victimRole == Role.INNOCENT) {
                    killer.sendMessage(Main.PREFIX + "§cDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§c ermordet!");
                    karmaManager.remove(killer, Settings.KARMA_REMOVED_DETECTIVE_KILL_INNOCENT);
                } else if(victimRole == Role.DETECTIVE) {
                    killer.sendMessage(Main.PREFIX + "§aDu hast einen " + victimRole.getChatColor() + victimRole.getName() + "§a getötet");
                    karmaManager.remove(killer, Settings.KARMA_REMOVED_DETECTIVE_KILL_TEAMMATE);
                }
                break;
        }

        victim.sendMessage(Main.PREFIX + "§7Du wurdest von " + killerRole.getChatColor() + killer.getName() + "§7 ermordet");
        victim.sendTitle("§cVorbei", "§cDu wurdest ermordet", 5, 65, 10);

        inGameState.checkGameEnd();

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) return;
        Player player = event.getPlayer();
        if(plugin.getPlayers().contains(player)) {
            InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
            plugin.getPlayers().remove(player);
            event.setQuitMessage(Main.PREFIX + "§c" + event.getPlayer().getName() + "§7 hat das Spiel verlassen");
            inGameState.checkGameEnd();
        }
    }


    //Not needed anymore, put into roleManager.runFakeArmor()
    @Deprecated
    private ChatColor getColorToSend(Player player, Player target) {

        switch (roleManager.getPlayerRole(player)) {
            case TRAITOR:
                switch (roleManager.getPlayerRole(target)) {
                    case TRAITOR:
                        return Role.TRAITOR.getChatColor();
                    case DETECTIVE:
                    case INNOCENT:
                        return Role.INNOCENT.getChatColor();
                }
                break;
            case DETECTIVE:
                return Role.DETECTIVE.getChatColor();
            case INNOCENT:
                return Role.INNOCENT.getChatColor();
            default:
                return null;
        }
        return null;
    }






}
