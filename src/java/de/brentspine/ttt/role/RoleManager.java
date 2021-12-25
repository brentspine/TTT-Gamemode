package de.brentspine.ttt.role;

import com.mojang.datafixers.util.Pair;
import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
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

        traitors = (int) Math.round(Math.log(playerAmount) * Settings.traitorProbability); //Traitor probability
        detectives = (int) Math.round(Math.log(playerAmount) * Settings.detectiveProbability); //Detectives probability
        innocents = playerAmount - traitors - detectives;

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

        for(Player current : players) {
            switch (getPlayerRole(current)) {
                case TRAITOR:
                    for(Player current2 : players)
                        setFakeArmor(current2, current.getEntityId(), (getPlayerRole(current2) != Role.TRAITOR) ? Color.GRAY : Color.RED);
                    break;
                case DETECTIVE:
                    setArmor(current, Color.BLUE);
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

    public void setFakeArmor(Player player, int entityID, Color color) {
        ItemStack armor = getColoredChestPlate(color);

        final List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipmentList = new ArrayList<>();

        equipmentList.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armor)));

        final PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment(entityID, equipmentList);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(entityEquipment);

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
