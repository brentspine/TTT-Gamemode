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
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.role.RoleManager;
import de.brentspine.ttt.util.ItemBuilder;
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
