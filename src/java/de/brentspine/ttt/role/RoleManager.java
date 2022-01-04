package de.brentspine.ttt.role;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.datafixers.util.Pair;
import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import org.bukkit.ChatColor;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RoleManager {

    private Main plugin;
    private HashMap<String, Role> playerRoles;
    private ArrayList<Player> players;
    private ArrayList<String> traitorPlayers;
    private ArrayList<String> detectivePlayers;

    private int traitors, detectives, innocents;

    public RoleManager(Main plugin) {
        this.plugin = plugin;
        playerRoles = new HashMap<>();
        players = plugin.getPlayers();
        traitorPlayers = new ArrayList<>();
        detectivePlayers = new ArrayList<>();
    }

    public void calculateRoles() {
        int playerAmount = players.size();

        traitors = (int) Math.round(Math.log(playerAmount) * Settings.TRAITOR_PROBABILITY); //Traitor probability
        detectives = (int) Math.round(Math.log(playerAmount) * Settings.DETECTIVE_PROBABILITY); //Detectives probability
        innocents = playerAmount - traitors - detectives;

        //traitors = 1;
        //detectives = 1;

        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Traitor: " + traitors);
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Detectives: " + detectives);
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Innocents: " + innocents);

        Collections.shuffle(players);

        int counter = 0;
        for (int i = counter; i < traitors; i++) {
            playerRoles.put(players.get(i).getName(), Role.TRAITOR);
            traitorPlayers.add(players.get(i).getName());
        }
        counter += traitors;

        for (int i = counter; i < detectives + counter; i++) {
            playerRoles.put(players.get(i).getName(), Role.DETECTIVE);
            detectivePlayers.add(players.get(i).getName());
        }
        counter += detectives;

        for (int i = counter; i < innocents + counter; i++)
            playerRoles.put(players.get(i).getName(), Role.INNOCENT);

        runFakeArmor();

        for(Player current : players) {
            switch (getPlayerRole(current)) {
                case TRAITOR:
                    setArmor(current, Color.RED);
                    current.getInventory().setItem(8, plugin.getRoleInventories().getTraitorItem());
                    break;
                case DETECTIVE:
                    setArmor(current, Color.BLUE);
                    current.getInventory().setItem(8, plugin.getRoleInventories().getDetectiveItem());
                    break;
                case INNOCENT:
                    setArmor(current, Color.GRAY);
                    break;
            }
        }
    }

    public void setArmor(Player player, Color color) {
        player.getInventory().setChestplate(getColoredChestPlate(color));
    }


    //Use runFakeArmor() instead.
    @Deprecated
    public void setFakeArmor(Player player, int entityID, Color color) {
        ItemStack armor = getColoredChestPlate(color);

        final List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipmentList = new ArrayList<>();

        equipmentList.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armor)));

        final PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment(entityID, equipmentList);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(entityEquipment);

    }

    public void runFakeArmor() {

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        final Color[] r = {null};

        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {

                PacketContainer packet = event.getPacket();
                EnumWrappers.ItemSlot itemSlot = packet.getSlotStackPairLists().read(0).get(0).getFirst();
                ItemStack itemStack = packet.getSlotStackPairLists().read(0).get(0).getSecond();
                Player target = event.getPlayer();
                Entity playerEntity = manager.getEntityFromID(target.getWorld(), packet.getIntegers().read(0));
                if(!(playerEntity instanceof Player)) {
                    return;
                }
                Player player = (Player) playerEntity;
                if(itemSlot == EnumWrappers.ItemSlot.CHEST) {
                    Role targetRole = getPlayerRole(target);
                    Role playerRole = getPlayerRole(player);
                    switch (playerRole) {
                        case TRAITOR:
                            switch (targetRole) {
                                case TRAITOR:
                                    r[0] = Role.TRAITOR.getChestPlateColor();
                                    break;
                                case DETECTIVE:
                                case INNOCENT:
                                    r[0] = Role.INNOCENT.getChestPlateColor();
                                    break;
                            }
                            break;
                        case DETECTIVE:
                            r[0] = Role.DETECTIVE.getChestPlateColor();
                            break;
                        case INNOCENT:
                            r[0] = Role.INNOCENT.getChestPlateColor();
                            break;
                        default:
                            r[0] = null;
                            break;
                    }
                    List<com.comphenix.protocol.wrappers.Pair<EnumWrappers.ItemSlot, ItemStack>> edit = packet.getSlotStackPairLists().read(0);
                    edit.get(0).setSecond(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(r[0]).build());
                    packet.getSlotStackPairLists().write(0, edit);
                }
            }
        });

    }

    private ItemStack getColoredChestPlate(Color color) {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color).build();
    }

    public Role getPlayerRole(Player player) {
        return playerRoles.get(player.getName());
    }

    public ArrayList<String> getTraitorPlayers() {
        return traitorPlayers;
    }

    public ArrayList<String> getDetectivePlayers() {
        return detectivePlayers;
    }

}
