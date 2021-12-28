package de.brentspine.ttt.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import de.brentspine.ttt.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class BoomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if(!player.isOp()) {
            player.sendMessage(Main.NO_PERMISSION);
            return true;
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        player.getLineOfSight(null, 50).stream()
                .filter(block -> block.getType() != Material.AIR)
                .forEach(block -> {

                    Location blockLocation = block.getLocation();
                    PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.EXPLOSION);
                    packet.getDoubles().write(0, blockLocation.getX());
                    packet.getDoubles().write(1, blockLocation.getY());
                    packet.getDoubles().write(2, blockLocation.getZ());

                    packet.getFloat().write(0, 5.0f);
                    packet.getFloat().write(1, 0f);
                    packet.getFloat().write(2, 3.0f);
                    packet.getFloat().write(3, 0f);

                    try {
                        protocolManager.sendServerPacket(player, packet);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                });

        return false;
    }

}
