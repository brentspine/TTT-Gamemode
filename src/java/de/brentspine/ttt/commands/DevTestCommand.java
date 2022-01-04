package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        //Main.instance.getTraitorCreeperArrow().test(player);
        player.sendMessage(Main.PREFIX + "§aSuccess");
        return false;
    }

}
